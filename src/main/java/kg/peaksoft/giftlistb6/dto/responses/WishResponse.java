package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishResponse {

    private Long id;
    private String wishName;
    private String linkToGift;
    private LocalDate dateOfHoliday;
    private String description;
    private String image;
    private Status wishStatus;
}
