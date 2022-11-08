package kg.peaksoft.giftlistb6.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class  CharityResponses {

    List<YourCharityResponse> yourCharityResponses;
    List<OtherCharityResponse> otherCharityResponses;
}
