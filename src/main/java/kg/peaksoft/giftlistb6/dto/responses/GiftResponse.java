package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.db.models.Gift;
import kg.peaksoft.giftlistb6.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GiftResponse {

    private Long id;
    private String giftName;
    private Status status;
    private LocalDate date;
    private String image;
    private ReservedUserResponse reservedUserResponse;

    public GiftResponse(Gift gift) {
        this.id = gift.getId();
        this.giftName = gift.getWish().getWishName();
        this.date = gift.getWish().getDateOfHoliday();
        this.status = gift.getWish().getWishStatus();
        this.image = gift.getWish().getImage();
        this.reservedUserResponse = new ReservedUserResponse(gift.getWish().getUser().getId(),
                gift.getWish().getUser().getFirstName() + " " + gift.getWish().getUser().getLastName(), gift.getWish().getUser().getImage());
    }
}