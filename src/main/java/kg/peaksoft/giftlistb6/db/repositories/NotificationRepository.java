package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.Notification;
import kg.peaksoft.giftlistb6.dto.responses.NotificationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("select new kg.peaksoft.giftlistb6.dto.responses.NotificationResponseForAdmin(" +
            "n.user.id," +
            "n.user.firstName," +
            "n.user.lastName," +
            "n.user.photo," +
            "n.wish.wishName)from Notification n")
    List<NotificationResponse>getAllNotificationForAdmin();
}
