package kg.peaksoft.giftlistb6.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FriendProfileResponse {

    private Long id;
    private String photo;
    private String country;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private int shoeSize;
    private String clothingSize;
    private String hobby;
    private String important;
    private List<WishResponses> wishResponses;
    private List<HolidayResponsess> holidayResponses;
    private List<CharityResponse> charityResponses;
}
