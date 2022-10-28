package kg.peaksoft.giftlistb6.dto.requests;

import kg.peaksoft.giftlistb6.validations.PasswordValid;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Valid
public class RegisterRequest {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @Email
    private String email;

    @NotBlank
    @NotNull
    @PasswordValid
    private String password;
}
