package kg.peaksoft.giftlistb6.api;

import com.google.firebase.auth.FirebaseAuthException;
import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.dto.requests.ResetPasswordRequest;
import kg.peaksoft.giftlistb6.dto.responses.AuthResponse;
import kg.peaksoft.giftlistb6.db.service.UserService;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/public")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthApi {

    private final UserService userService;

    @PostMapping("register")
    public AuthResponse register(@RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return userService.login(authRequest);
    }

    @PostMapping("/authenticate/google")
    public AuthResponse authWithGoogle(String tokenId) throws FirebaseAuthException {
        return userService.authWithGoogle(tokenId);
    }

    @PostMapping("/forgot/password")
    public SimpleResponse forgotPassword(@RequestParam String email,
                                         @RequestParam String link) throws MessagingException {
        return userService.forgotPassword(email,link);
    }

    @PostMapping("/resetPassword")
    public SimpleResponse resetPassword(@RequestBody ResetPasswordRequest request){
        return userService.resetPassword(request);
    }
}
