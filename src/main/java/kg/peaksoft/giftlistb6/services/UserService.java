package kg.peaksoft.giftlistb6.services;

import kg.peaksoft.giftlistb6.configs.security.JwtUtils;
import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.dto.responses.AuthResponse;
import kg.peaksoft.giftlistb6.entities.User;
import kg.peaksoft.giftlistb6.enums.Role;
import kg.peaksoft.giftlistb6.exseptions.NotFoundException;
import kg.peaksoft.giftlistb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;


    public AuthResponse register(RegisterRequest registerRequest) {

        if(userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new NotFoundException("this email: " + registerRequest.getEmail() + " is already in use!");
        }

        User user = convertToRegisterEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        userRepo.save(user);

        String jwt = jwtUtils.generateToken(user.getEmail());

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                jwt
        );
    }

    public AuthResponse login(AuthRequest authRequest) {

        User user = userRepo.findByEmail(authRequest.getEmail()).orElseThrow(() -> new NotFoundException("user with this email: " + authRequest.getEmail() + " not found!"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("bad credential");
        }

        String jwt = jwtUtils.generateToken(user.getEmail());

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                jwt
        );
    }

    public User convertToRegisterEntity(RegisterRequest registerRequest)  {
        return User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();
    }

    public AuthResponse convertToView(User user) {
        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public List<AuthResponse> convertEntitiesToView(List<User> users) {
        List<AuthResponse> allUsers = new ArrayList<>();
        for (User user : users) {
            allUsers.add(convertToView(user));
        }
        return allUsers;
    }
}
