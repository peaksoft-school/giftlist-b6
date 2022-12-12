package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.google.firebase.auth.FirebaseAuthException;
import kg.peaksoft.giftlistb6.dto.requests.AuthRequest;
import kg.peaksoft.giftlistb6.dto.requests.RegisterRequest;
import kg.peaksoft.giftlistb6.dto.requests.ResetPasswordRequest;
import kg.peaksoft.giftlistb6.dto.responses.AuthResponse;
import kg.peaksoft.giftlistb6.db.services.UserService;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/public")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Auth Api", description = "Authentication and Authorization")
public class AuthApi {

    private final UserService userService;

    @Operation(summary = "Sign up",description = "Any user can register")
    @PostMapping("register")
    public AuthResponse register(@RequestBody @Valid RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @Operation(summary = "Sign in",description = "Only registered users can login")
    @PostMapping("login")
    public AuthResponse login(@RequestBody @Valid AuthRequest authRequest) throws MessagingException {
        return userService.login(authRequest);
    }

    @Operation(summary = "Google authentication", description = "Authenticate with google")
    @PostMapping("auth-google")
    public AuthResponse authWithGoogle(String tokenId) throws FirebaseAuthException {
        return userService.authWithGoogle(tokenId);
    }

    @Operation(summary = "Forgot password", description = "Send link forgot password")
    @PostMapping("forgot-password")
    public SimpleResponse forgotPassword(@RequestParam String email,
                                         @RequestParam String link) throws MessagingException {
        return userService.forgotPassword(email,link);
    }

    @Operation(summary = "Reset Password", description = "Change password")
    @PostMapping("reset-password/{id}")
    public SimpleResponse resetPassword(@PathVariable("id") Long id,  @RequestBody @Valid ResetPasswordRequest request){
        return userService.resetPassword(id,request);
    }
}
