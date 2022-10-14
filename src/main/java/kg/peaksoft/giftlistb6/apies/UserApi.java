package kg.peaksoft.giftlistb6.apies;

import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.dto.responses.AuthResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/public")
@CrossOrigin
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

    @GetMapping("/forgot/password")
    public SimpleResponse forgotPassword(@RequestParam String email,
                                         @RequestParam String link) throws MessagingException {
        return userService.forgotPassword(email,link);
    }

    @PostMapping("/resetPassword/{id}")
    public SimpleResponse resetPassword(@PathVariable Long id,String newPassword){
        return userService.resetPassword(id,newPassword);
    }
}
