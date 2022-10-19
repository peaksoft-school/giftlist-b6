package kg.peaksoft.giftlistb6.dto.requests;

import kg.peaksoft.giftlistb6.db.model.Holiday;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

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
