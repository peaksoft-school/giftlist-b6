package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendWishesResponse {

    private Long id;
    private String wishName;
    private String holidayName;
    private String linkToGift;
    private LocalDate dateOfHoliday;
    private String description;
    private String image;
    private Status wishStatus;
    private Boolean isMy;
    private Boolean isBlock;
    private ReservedUserResponse reservedUserResponse;
}