package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.db.model.Holiday;
import kg.peaksoft.giftlistb6.db.model.User;
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
public class WishResponse {

    private Long id;

    private String wishName;

    private HolidayResponse holiday;

    private Status wishStatus;
}
