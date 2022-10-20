package kg.peaksoft.giftlistb6.db.repository;

import kg.peaksoft.giftlistb6.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<User, Long> {

}
