package kg.peaksoft.giftlistb6.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintRequest {
    private String complaintText;
    private Long wishId;
}
