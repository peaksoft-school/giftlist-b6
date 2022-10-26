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
public class WishResponse1 {
    private Long id;
    private String wishName;
    private String linkToGift;
    private LocalDate dateOfHoliday;
    private String description;
    private String image;
    private Status wishStatus;
    private ReservedUserResponse reservedUserResponse;


    public WishResponse1(Wish wish){
        this.id=wish.getId();
        this.wishName=wish.getWishName();
        this.linkToGift=wish.getLinkToGift();
        this.dateOfHoliday=wish.getDateOfHoliday();
        this.description=wish.getDescription();
        this.image=wish.getImage();
        this.wishStatus=Status.RESERVED;
        if (wish.getReservoir()!=null){
            this.reservedUserResponse=new ReservedUserResponse(wish.getReservoir().getId(),
                    wish.getReservoir().getFirstName()+""+ wish.getReservoir().getLastName(),
                    wish.getImage());
        }else {
            this.reservedUserResponse=null;
        }
    }
}
