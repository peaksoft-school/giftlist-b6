package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishResponse {

    private Long id;
    private String wishName;
    private String image;
    private HolidayResponse holiday;
    private Status wishStatus;

    public WishResponse(Wish wish) {
        this.id = wish.getId();
        this.wishName = wish.getWishName();
        this.image = wish.getImage();
        this.wishStatus = wish.getWishStatus();
        this.holiday = new HolidayResponse(wish.getHoliday().getName(),
                wish.getHoliday().getDateOfHoliday());
    }
}