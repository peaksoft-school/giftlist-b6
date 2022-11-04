package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InnerFeedResponse {

    private Long wishId;
    private SearchUserResponse searchUserResponse;
    private HolidayResponse holidayResponse;
    private String wishName;
    private Status status;
    private String description;
}
