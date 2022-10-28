package kg.peaksoft.giftlistb6.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HolidayResponses {

    private Long id;
    private String name;
    private LocalDate dateOfHoliday;
    private String image;
    private Long user;

    public HolidayResponses(Long id, String name, LocalDate dateOfHoliday, String image) {
        this.id = id;
        this.name = name;
        this.dateOfHoliday = dateOfHoliday;
        this.image = image;
    }
}
