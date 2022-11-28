package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.db.models.Charity;
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
    private Status giftStatus;
    private ReservedUserResponse reservedUserResponse;

    public GiftResponse(Gift gift) {
        this.id = gift.getId();
        this.giftName = gift.getWish().getWishName();
        this.date = gift.getWish().getDateOfHoliday();
        this.status = gift.getWish().getWishStatus();
        this.image = gift.getWish().getImage();
        this.giftStatus = Status.GIFT;
        this.reservedUserResponse = new ReservedUserResponse(gift.getWish().getUser().getId(),
                gift.getWish().getUser().getFirstName() + " " + gift.getWish().getUser().getLastName(), gift.getWish().getUser().getImage());
    }

    public GiftResponse (Charity charity ){
        this.giftName=charity.getName();
        this.id = charity.getId();
        this.date = charity.getCreatedAt();
        this.image = charity.getImage();
        this.status = charity.getCharityStatus();
        this.giftStatus=Status.CHARITY;
        if (charity.getReservoir()!=null){
        this.reservedUserResponse= new ReservedUserResponse(charity.getReservoir().getId(),
                charity.getReservoir().getFirstName()+" "+charity.getReservoir().getLastName(),charity.getReservoir().getImage());
        }else {
            this.reservedUserResponse = new ReservedUserResponse();
        }
    }
}