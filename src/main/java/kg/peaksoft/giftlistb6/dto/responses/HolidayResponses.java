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
    private Boolean isBlock;
    private Long user;

    public HolidayResponses(Long id, String name, LocalDate dateOfHoliday, String image) {
        this.id = id;
        this.name = name;
        this.dateOfHoliday = dateOfHoliday;
        this.image = image;
    }

    public HolidayResponses(Long id, String name, LocalDate dateOfHoliday, String image, Boolean isBlock) {
        this.id = id;
        this.name = name;
        this.dateOfHoliday = dateOfHoliday;
        this.image = image;
        this.isBlock = isBlock;
    }

    public HolidayResponses(Long id, String name, LocalDate dateOfHoliday, String image, Long user) {
        this.id = id;
        this.name = name;
        this.dateOfHoliday = dateOfHoliday;
        this.image = image;
        this.user = user;
    }
}
