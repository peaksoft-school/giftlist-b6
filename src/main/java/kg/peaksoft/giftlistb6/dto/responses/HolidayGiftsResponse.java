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
public class HolidayGiftsResponse {

    private Long id;
    private String wishName;
    private String holidayName;
    private String linkToGift;
    private LocalDate dateOfHoliday;
    private String description;
    private String image;
    private Status wishStatus;

    public HolidayGiftsResponse(Long id, String wishName, String linkToGift, LocalDate dateOfHoliday, String description, String image, Status wishStatus) {
        this.id = id;
        this.wishName = wishName;
        this.linkToGift = linkToGift;
        this.dateOfHoliday = dateOfHoliday;
        this.description = description;
        this.image = image;
        this.wishStatus = wishStatus;
    }
}