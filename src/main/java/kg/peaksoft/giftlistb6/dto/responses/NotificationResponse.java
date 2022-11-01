package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String fullName;
    private String photo;
    private LocalDate createdDate;
    private NotificationType notificationType;
    private String message;
}
