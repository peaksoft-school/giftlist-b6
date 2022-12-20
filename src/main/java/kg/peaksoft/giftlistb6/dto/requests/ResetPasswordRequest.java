package kg.peaksoft.giftlistb6.dto.requests;

import kg.peaksoft.giftlistb6.validations.PasswordValid;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank
    @NotNull
    private String oldPassword;

    @NotNull
    @NotBlank
    @PasswordValid
    private String newPassword;
}
