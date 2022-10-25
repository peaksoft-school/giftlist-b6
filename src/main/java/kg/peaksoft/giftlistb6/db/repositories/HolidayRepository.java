package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.Holiday;
import kg.peaksoft.giftlistb6.dto.responses.HolidayResponses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    @Query("select new kg.peaksoft.giftlistb6.dto.responses.HolidayResponses(" +
            "h.id," +
            "h.name," +
            "h.dateOfHoliday," +
            "h.image," +
            "h.user.id) from User u join u.holidays h where u.email = ?1")
    List<HolidayResponses> getAllHolidays(String email);

    @Modifying
    @Transactional
    @Query("update Wish " +
            "set holiday  = null where id=?1")
    void deleteByWishId(Long id);
}