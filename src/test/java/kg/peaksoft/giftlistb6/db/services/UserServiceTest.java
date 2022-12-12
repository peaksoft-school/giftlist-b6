package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.configs.security.JwtUtils;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.exceptions.BadCredentialsException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserService underTest;

    @Test
    @DisplayName("register success")
    void shouldSaveTheUser_WhenTheEmailIs_NotTaken() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("Jaulan")
                .lastName("Nurkamal uulu")
                .password("jaulan")
                .email("jaulan@gmail.com")
                .build();

        jwtUtils.generateToken(registerRequest.getEmail());
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        underTest.register(registerRequest);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("register fail")
    void shouldThrowAnException_WhenTheEmail_IsAlreadyTaken() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("Jaulan")
                .lastName("Nurkamal uulu")
                .password("jaulan")
                .email("jaulan@gmail.com")
                .build();

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);
        assertThrows(BadCredentialsException.class, () -> underTest.register(registerRequest));
    }

    @Test
    @DisplayName("incorrect password")
    @SneakyThrows
    void ShouldThrowAnException_WhenThePassword_IsIncorrect() {

        AuthRequest authRequest = AuthRequest.builder()
                .email("aiza@gmail.com")
                .password("aiza").build();

        User user = User.builder().password("jaulan").build();

        assertNotEquals(authRequest.getPassword(), user.getPassword());
        boolean matches = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        assertFalse(matches);
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(user));
        assertThrows(BadCredentialsException.class, () -> underTest.login(authRequest));
    }

    @Test
    @DisplayName("login fail")
    @SneakyThrows
    void throwAnException_IfUserDoesNotExists_In_A_Database() {

        AuthRequest authRequest = AuthRequest
                .builder()
                .password("aiza")
                .email("aiza@gmail.com")
                .build();

        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.empty());

        try {
            underTest.login(authRequest);
            Assertions.fail("Our method does not throw an exception");
        } catch (Exception ignored) {
        }
    }
}