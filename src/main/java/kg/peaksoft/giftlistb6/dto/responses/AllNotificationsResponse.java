package kg.peaksoft.giftlistb6.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllNotificationsResponse {

    private List<NotificationResponse> responseList;
}
