package kg.peaksoft.giftlistb6.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCharityResponse {

    private Long id;
    private String fistName;
    private String lastName;
    private String image;
    private String phoneNumber;

    public UserCharityResponse(Long id, String fistName, String lastName, String image) {
        this.id = id;
        this.fistName = fistName;
        this.lastName = lastName;
        this.image = image;
    }
}
