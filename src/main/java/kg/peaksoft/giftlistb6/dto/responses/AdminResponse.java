package kg.peaksoft.giftlistb6.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponse {

    private Long id;
    private String first_name;
    private String last_name;
    private String photo;
    private int countGift;
    private Boolean isBlock;
}