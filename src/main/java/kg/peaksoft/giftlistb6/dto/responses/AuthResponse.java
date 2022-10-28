package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthResponse {

    private Long id;
    private String firstName;
    private String lastname;
    private String email;
    private Role role;

    private String jwt;


    public AuthResponse(Long id, String firstName, String lastName, String email, String email1, Role role, String token) {
        this.id = id;
        this.firstName = firstName;
        this.lastname = lastName;
        this.email = email;
        this.role = role;
    }
}
