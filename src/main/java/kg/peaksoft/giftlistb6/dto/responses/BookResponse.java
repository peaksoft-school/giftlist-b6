package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.db.models.Wish;
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
public class BookResponse {

    private Long id;
    private String wishName;
    private String holidayName;
    private LocalDate dateOfHoliday;
    private String image;
    private Status wishStatus;
    private ReservedUserResponse reservedUserResponse;


    public BookResponse(Wish wish) {
        this.id = wish.getId();
        this.wishName = wish.getWishName();
        this.holidayName = wish.getHoliday().getName();
        this.dateOfHoliday = wish.getDateOfHoliday();
        this.image = wish.getImage();
        this.wishStatus = wish.getWishStatus();
        if (wish.getReservoir() != null) {
            this.reservedUserResponse = new ReservedUserResponse(wish.getReservoir().getId(),
                    wish.getReservoir().getFirstName() + " " + wish.getReservoir().getLastName(),
                    wish.getImage());
        } else {
            this.reservedUserResponse = new ReservedUserResponse();
        }
    }
}