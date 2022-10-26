package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {

    void deleteByWishId(Long id);
}