package kg.peaksoft.giftlistb6.dto.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
