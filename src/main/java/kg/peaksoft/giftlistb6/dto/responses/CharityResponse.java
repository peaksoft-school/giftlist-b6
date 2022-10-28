package kg.peaksoft.giftlistb6.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CharityResponse {
    List<OtherCharityResponse> otherCharities;
    List<YourCharityResponse> yourCharities;
}
