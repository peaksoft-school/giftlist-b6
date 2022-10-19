package kg.peaksoft.giftlistb6.db.repository;

import kg.peaksoft.giftlistb6.db.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
}