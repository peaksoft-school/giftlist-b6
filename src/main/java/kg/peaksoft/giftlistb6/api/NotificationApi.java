package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.NotificationService;
import kg.peaksoft.giftlistb6.dto.responses.AllNotificationsResponse;
import kg.peaksoft.giftlistb6.dto.responses.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAuthority('USER')")
@RequestMapping("/api/notifications")
@Tag(name = "Notification Api", description = "All notifications")
public class NotificationApi {

    private final NotificationService notificationService;

    @Operation(summary = "User notifications", description = "User can see all notifications")
    @GetMapping
    public AllNotificationsResponse getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @Operation(summary = "Mark as read", description = "User can mark as read all notifications")
    @PostMapping
    public AllNotificationsResponse isRead() {
        return notificationService.markAsRead();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Admin notifications", description = "Admin can see all notifications")
    @GetMapping("/notifications")
    public List<NotificationResponse> getAllNotificationForAdmin(){
        return notificationService.getAllNotificationsForAdmin();
    }
}