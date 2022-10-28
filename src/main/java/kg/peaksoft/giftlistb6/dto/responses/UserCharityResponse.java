package kg.peaksoft.giftlistb6.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCharityResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String photo;
}
