package kg.peaksoft.giftlistb6.db.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import kg.peaksoft.giftlistb6.configs.security.JwtUtils;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.dto.requests.ResetPasswordRequest;
import kg.peaksoft.giftlistb6.dto.responses.AuthResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.enums.Role;
import kg.peaksoft.giftlistb6.exceptions.BadCredentialsException;
import kg.peaksoft.giftlistb6.exceptions.BadRequestException;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;


    public AuthResponse register(RegisterRequest registerRequest) {
        User user = convertToRegisterEntity(registerRequest);
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new BadCredentialsException(String.format("Пользователь с этим электронным адресом: %s уже существует!",registerRequest.getEmail()));
        } else {
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole(Role.USER);
            userRepo.save(user);

            String jwt = jwtUtils.generateToken(user.getEmail());

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

    public AuthResponse login(AuthRequest authRequest) {

        if (authRequest.getPassword().isBlank()) {
            throw new BadRequestException("Пароль не может быть пустым!");
        }
        User user = userRepo.findByEmail(authRequest.getEmail()).orElseThrow(
                () -> new NotFoundException(String.format("Пользовотель с таким электронным адресом:  %s не найден!",authRequest.getEmail())));
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Неверный пароль!");
        }

        String jwt = jwtUtils.generateToken(user.getEmail());

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
            newUser.setFirstName(firebaseToken.getName());
            newUser.setEmail(firebaseToken.getEmail());
            newUser.setPassword(firebaseToken.getEmail());
            newUser.setRole(Role.USER);
            user = userRepo.save(newUser);
        }
        user = userRepo.findByEmail(firebaseToken.getEmail()).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким электронным адресом %s не найден!",firebaseToken.getEmail())));
        String token = jwtUtils.generateToken(user.getPassword());
        return new AuthResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getEmail(), user.getRole(), token);

    }

    public SimpleResponse forgotPassword(String email, String link) throws MessagingException {
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким электронным адресом %s не найден!",email)));
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setSubject("[gift_list] reset password link");
        helper.setFrom("giftlistb66@gmail.com");
        helper.setTo(email);
        helper.setText(link + "/" + user.getId(), true);
        mailSender.send(mimeMessage);
        return new SimpleResponse("Отправлено", "Ок");
    }

    public SimpleResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepo.findById(request.getId()).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким id: %s не найден!",request.getId()))
        );
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return new SimpleResponse("Пароль обновлен", "ок");
    }
}
