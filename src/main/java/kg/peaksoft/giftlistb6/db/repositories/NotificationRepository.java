package kg.peaksoft.giftlistb6.db.repository;

import kg.peaksoft.giftlistb6.db.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

}
