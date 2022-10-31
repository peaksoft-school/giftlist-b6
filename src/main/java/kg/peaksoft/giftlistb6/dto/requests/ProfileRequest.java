package kg.peaksoft.giftlistb6.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileRequest {

    private String photo;
    private String country;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private int shoeSize;
    private String clothingSize;
    private String hobby;
    private String important;
}
