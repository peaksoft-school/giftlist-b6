package kg.peaksoft.giftlistb6.apies;

import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.dto.responses.AuthResponse;
import kg.peaksoft.giftlistb6.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserApi {

    private final UserService userService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return userService.login(authRequest);
    }
}
