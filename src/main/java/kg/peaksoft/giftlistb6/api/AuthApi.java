package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.google.firebase.auth.FirebaseAuthException;
import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.dto.requests.ResetPasswordRequest;
import kg.peaksoft.giftlistb6.dto.responses.AuthResponse;
import kg.peaksoft.giftlistb6.db.service.UserService;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/public")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Auth Api", description = "Authentication and Authorization")
public class AuthApi {

    private final UserService userService;

    @Operation(summary = "Sign up",description = "Any user can register")
    @PostMapping("register")
    public AuthResponse register(@RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @Operation(summary = "Sign in",description = "Only registered users can login")
    @PostMapping("login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return userService.login(authRequest);
    }

    @Operation(summary = "Google authentication", description = "Authenticate with google")
    @PostMapping("/authenticate/google")
    public AuthResponse authWithGoogle(String tokenId) throws FirebaseAuthException {
        return userService.authWithGoogle(tokenId);
    }

    @Operation(summary = "Forgot password", description = "Send link forgot password")
    @PostMapping("/forgot/password")
    public SimpleResponse forgotPassword(@RequestParam String email,
                                         @RequestParam String link) throws MessagingException {
        return userService.forgotPassword(email,link);
    }

    @Operation(summary = "Reset Password", description = "Change password")
    @PostMapping("/resetPassword")
    public SimpleResponse resetPassword(@RequestBody ResetPasswordRequest request){
        return userService.resetPassword(request);
    }
}
