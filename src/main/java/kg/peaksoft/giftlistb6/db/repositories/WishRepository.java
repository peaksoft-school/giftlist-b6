package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.dto.responses.BookResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.BookResponse(w) from Wish w where w.user.email = ?1")
    List<BookResponse> getALlReservoirWishes(String email);
}