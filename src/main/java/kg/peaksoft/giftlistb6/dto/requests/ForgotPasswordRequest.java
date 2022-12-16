package kg.peaksoft.giftlistb6.dto.requests;

import kg.peaksoft.giftlistb6.validations.PasswordValid;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ForgotPasswordRequest {

    private Long id;

    @NotNull
    @NotBlank
    private String newPassword;

    @NotNull
    @NotBlank
    @PasswordValid
    private String verifyPassword;
}