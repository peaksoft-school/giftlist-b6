package kg.peaksoft.giftlistb6.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WishRequest {

    private String wishName;
    private String linkToGift;
    private Long holidayId;
    private LocalDate dateOfHoliday;
    private String description;
    private String image;
}
