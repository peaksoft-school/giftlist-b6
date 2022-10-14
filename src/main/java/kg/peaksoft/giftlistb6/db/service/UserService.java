package kg.peaksoft.giftlistb6.db.service;

import kg.peaksoft.giftlistb6.config.security.JwtUtils;
import kg.peaksoft.giftlistb6.db.model.User;
import kg.peaksoft.giftlistb6.db.repository.UserRepository;
import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.dto.requests.ResetPasswordRequest;
import kg.peaksoft.giftlistb6.dto.responses.AuthResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.enums.Role;
import kg.peaksoft.giftlistb6.exception.BadCredentialsException;
import kg.peaksoft.giftlistb6.exception.BadRequestException;
import kg.peaksoft.giftlistb6.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepo;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;


    public AuthResponse register(RegisterRequest registerRequest) {

        if (registerRequest.getPassword().isBlank()) {
            throw new BadRequestException("password can not be empty!");
        }
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("this email: " + registerRequest.getEmail() + " is already in use!");
        }

        User user = convertToRegisterEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        userRepo.save(user);

        String jwt = jwtUtils.generateToken(user.getEmail());
        System.out.println(user.getFirstName());

        return new AuthResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                jwt
        );
    }

    public AuthResponse login(AuthRequest authRequest) {

        if (authRequest.getPassword().isBlank()) {
            throw new BadRequestException("password can not be empty!");
        }

        User user = userRepo.findByEmail(authRequest.getEmail()).orElseThrow(() -> new NotFoundException("user with this email: " + authRequest.getEmail() + " not found!"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("incorrect password");
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

    public SimpleResponse forgotPassword(String email, String link) throws MessagingException {
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new NotFoundException("user not found"));
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setSubject("[gift_list] reset password link");
        helper.setFrom("giftlistb66@gmail.com");
        helper.setTo(email);
        helper.setText(link + "/" + user.getId(), true);
        mailSender.send(mimeMessage);
        return new SimpleResponse("email send", "ok");
    }

    public SimpleResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepo.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("user not found")
        );
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return new SimpleResponse("password updated", "ok");
    }
}
