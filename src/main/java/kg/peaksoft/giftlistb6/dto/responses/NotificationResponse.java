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

    private Long userId;
    private String firstName;
    private String lastName;
    private String photo;
    private LocalDate createdAt;
    private NotificationType notificationType;
    private String message;
}
