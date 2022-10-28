package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.Gift;
import kg.peaksoft.giftlistb6.dto.responses.GiftResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {

    void deleteByWishId(Long id);

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.GiftResponse(" +
            "g.id," +
            "concat( g.wish.reservoir.firstName,' ',g.wish.reservoir.lastName)," +
            "g.wish.wishStatus," +
            "g.wish.dateOfHoliday," +
            "g.wish.image," +
            "g.wish.wishName) from Gift g join g.wish w join g.user u where u.email =?1 ")
    List<GiftResponse> getAllGifts(String email);
}