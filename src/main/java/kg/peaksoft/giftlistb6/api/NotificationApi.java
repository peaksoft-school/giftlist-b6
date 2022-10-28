package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.NotificationService;
import kg.peaksoft.giftlistb6.dto.responses.AllNotificationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/notifications")
@Tag(name = "Notification Api", description = "All notifications")
public class NotificationApi {

    private final NotificationService notificationService;

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "all user notifications",description = "User can see all notifications")
    @GetMapping
    public AllNotificationsResponse getAllNotifications(){
        return notificationService.getAllNotifications();
    }

}
