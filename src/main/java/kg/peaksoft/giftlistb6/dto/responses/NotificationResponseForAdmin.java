package kg.peaksoft.giftlistb6.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationResponseForAdmin {

    private Long userId;
    private String firstName;
    private String lastName;
    private String userPhoto;
    private String wishName;
}
