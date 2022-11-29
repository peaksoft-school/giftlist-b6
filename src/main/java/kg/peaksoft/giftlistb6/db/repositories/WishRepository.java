package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.dto.responses.BookResponse;
import kg.peaksoft.giftlistb6.dto.responses.WishResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.BookResponse(w) from Wish w where w.user.email = ?1 and w.wishStatus = 'RESERVED'")
    List<BookResponse> getALlReservoirWishes(String email);

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.WishResponse (w) from Wish w where w.user.email=?1")
    List<WishResponse> getAllWish(String email);

    @Query("select w from Wish w where w.user.isBlock = false and w.user.email <> ?1")
    List<Wish> getAllWishes(String email);

    @Query("select w from Wish w where w.user.friends = :user")
    List<Wish> getFriendsWishes(User user);

    @Query("select w from Wish w where w.user.isBlock = false and w.id = :id")
    Optional<Wish> findWishById(Long id);

    @Modifying
    @Query("delete from Wish w where w.id = :id")
    void deleteById(Long id);
}
