package kg.peaksoft.giftlistb6.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileResponse {

    private Long id;
    private String image;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private int shoeSize;
    private String clothingSize;
    private String hobby;
    private String important;
    private String facebookLink;
    private String instagramLink;
    private String telegramLink;
    private String vkLink;
}
