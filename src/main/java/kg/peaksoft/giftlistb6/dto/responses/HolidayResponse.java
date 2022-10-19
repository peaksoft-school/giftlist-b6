package kg.peaksoft.giftlistb6.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class HolidayResponse {

    private String name;

    private LocalDate localDate;
}
