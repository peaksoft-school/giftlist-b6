package kg.peaksoft.giftlistb6.db.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import kg.peaksoft.giftlistb6.configs.security.JwtUtils;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.ForgotPasswordRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.dto.requests.ResetPasswordRequest;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.enums.Role;
import kg.peaksoft.giftlistb6.exceptions.BadCredentialsException;
import kg.peaksoft.giftlistb6.exceptions.BadRequestException;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepo;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @PostConstruct
    void init() throws IOException {
        GoogleCredentials googleCredentials =
                GoogleCredentials.fromStream(new ClassPathResource("giftlist.json").getInputStream());

        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(firebaseOptions);
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = convertToRegisterEntity(registerRequest);
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            log.error("User with email:{} if exist", registerRequest.getEmail());
            throw new BadCredentialsException(String.format("Пользователь с этим электронным адресом: %s уже существует!", registerRequest.getEmail()));
        } else {
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole(Role.USER);
            user.setIsBlock(false);
            userRepo.save(user);

            String jwt = jwtUtils.generateToken(user.getEmail());
            log.info("User with email:{} successfully registered", registerRequest.getEmail());
            return new AuthResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole(),
                    jwt
            );
        }
    }

    public AuthResponse login(AuthRequest authRequest) throws MessagingException {
        if (authRequest.getPassword().isBlank()) {
            log.error("Password cannot be empty!");
            throw new BadRequestException("Пароль не может быть пустым!");
        }
        User user = userRepo.findByEmail(authRequest.getEmail()).orElseThrow(
                () -> {
                    log.error("User with email:{} not found", authRequest.getEmail());
                    throw new NotFoundException(String.format("Пользовотель с таким электронным адресом:  %s не найден!", authRequest.getEmail()));
                });
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            log.error("Invalid password!");
            throw new BadCredentialsException("Неверный пароль!");
        }
        if (user.getIsBlock().equals(true)) {
            String message = "для разблокировки вашего аккаунта обратитесь к администратору по nurgazyn03@gmail.com";
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject("[gift_list]");
            helper.setFrom("giftlistb66@gmail.com");
            helper.setTo(authRequest.getEmail());
            helper.setText(message, true);
            mailSender.send(mimeMessage);
            log.error("A message was sent to the user's email {}", authRequest.getEmail());
            throw new BadRequestException("ваш аккаунт заблокирован,на ваш электронный адрес было отправлено письмо!");
        }
        String jwt = jwtUtils.generateToken(user.getEmail());
        log.info("User with email: {} successfully logged ", authRequest.getEmail());

        return new AuthResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                jwt
        );
    }

    public User convertToRegisterEntity(RegisterRequest registerRequest) {
        return User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();
    }

    public AuthResponse authWithGoogle(String tokenId) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(tokenId);
        User user;
        if (!userRepo.existsByEmail(firebaseToken.getEmail())) {
            User newUser = new User();
            String[] name = firebaseToken.getName().split(" ");
            newUser.setFirstName(name[0]);
            newUser.setLastName(name[1]);
            newUser.setEmail(firebaseToken.getEmail());
            newUser.setPassword(firebaseToken.getEmail());
            newUser.setRole(Role.USER);
            user = userRepo.save(newUser);
        }
        user = userRepo.findByEmail(firebaseToken.getEmail()).orElseThrow(
                () -> {
                    log.error("User with email: {} not found", firebaseToken.getEmail());
                    throw new NotFoundException(String.format("Пользователь с таким электронным адресом %s не найден!", firebaseToken.getEmail()));
                });
        String token = jwtUtils.generateToken(user.getPassword());
        log.info("User with email: {} successfully authenticate with google ", firebaseToken.getEmail());
        return new AuthResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), token);
    }

    public SimpleResponse forgotPassword(String email, String link) throws MessagingException {
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> {
                    log.error("User with email:{} not found!", email);
                    throw new NotFoundException(String.format("Пользователь с таким электронным адресом %s не найден!", email));
                });
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setSubject("[gift_list] reset password link");
        helper.setFrom("giftlistb66@gmail.com");
        helper.setTo(email);
        helper.setText(link + "/" + user.getId(), true);
        mailSender.send(mimeMessage);
        log.info("A message was sent to the user's email {}", email);
        return new SimpleResponse("Отправлено", "Ок");
    }

    @Transactional
    public SimpleResponse changeOnForgot(ForgotPasswordRequest password) {
        if (!password.getNewPassword().equals(password.getVerifyPassword())) {
            return new SimpleResponse("passwords does not matches", "INCORRECT");
        }
        User user = userRepo.findById(password.getId()).orElseThrow(() -> new NotFoundException("not found"));
        user.setPassword(passwordEncoder.encode(password.getVerifyPassword()));
        return new SimpleResponse("Changed", "OK");
    }

    @Transactional
    public SimpleResponse resetPassword(Long id, ResetPasswordRequest request) {
        User user = userRepo.findById(id).orElseThrow(
                () -> {
                    log.error("User with id: {} not found!", id);
                    throw new NotFoundException(String.format("Пользователь с таким id: %s не найден!", id));
                }
        );
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return new SimpleResponse("неверный старый пароль", "CREDENTIALS");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        log.info("The user's with id:{} password was successfully updated", id);
        return new SimpleResponse("Пароль обновлен", "ок");
    }

    public AdminResponse createUser(User user) {
        if (user == null) {
            return null;
        }
        AdminResponse adminUserGetAllResponse = new AdminResponse();
        adminUserGetAllResponse.setId(user.getId());
        adminUserGetAllResponse.setGiftCount(user.getGifts().size());
        adminUserGetAllResponse.setFirstName(user.getFirstName());
        adminUserGetAllResponse.setLastName(user.getLastName());
        adminUserGetAllResponse.setPhoto(user.getImage());
        adminUserGetAllResponse.setIsBlock(user.getIsBlock());
        return adminUserGetAllResponse;
    }

    public List<SearchUserResponse> searchUser(String text) {
        List<SearchUserResponse> userResponses = userRepo.search(convertCyrillic(text.toUpperCase()));
        userResponses.removeIf(response -> response.getUserId() == 1);
        return userResponses;
    }

    public static String convertCyrillic(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'ѓ', 'е', 'ж', 'з', 'ѕ', 'и', 'ј', 'к', 'л', 'љ', 'м', 'н', 'њ', 'о', 'п', 'р', 'с', 'т', 'ќ', 'у', 'ф', 'х', 'ц', 'ч', 'џ', 'ш', 'А', 'Б', 'В', 'Г', 'Д', 'Ѓ', 'Е', 'Ж', 'З', 'Ѕ', 'И', 'Ј', 'К', 'Л', 'Љ', 'М', 'Н', 'Њ', 'О', 'П', 'Р', 'С', 'Т', 'Ќ', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Џ', 'Ш', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/', '-'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "]", "e", "zh", "z", "y", "i", "j", "k", "l", "q", "m", "n", "w", "o", "p", "r", "s", "t", "'", "u", "f", "h", "c", ";", "x", "{", "A", "B", "V", "G", "D", "}", "E", "Zh", "Z", "Y", "I", "J", "K", "L", "Q", "M", "N", "W", "O", "P", "R", "S", "T", "KJ", "U", "F", "H", "C", ":", "X", "{", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "/", "-"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }
}