package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Holiday;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.enums.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.yml")
class WishServiceTest {

    @Autowired
    private WishRepository wishRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    void saveWish() {
        Wish wish = new Wish(1L,
                "Phone", "http://link-gift",
                LocalDate.of(2022, 11, 18),
                "description",
                "image",
                Status.RESERVED,
                true,
                new User(),
                new ArrayList<>(),
                new User(),
                new Holiday());
        Assertions.assertThat(wish.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    void findById() {
        Wish wish = wishRepository.findById(1L).get();
        Assertions.assertThat(wish.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    void findAll() {
        List<Wish> wishes = wishRepository.findAll();
        Assertions.assertThat(wishes.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    void update() {
        Wish wish = wishRepository.findById(1L).get();
        wish.setWishName("Phone");
        Wish wishUpdated = wishRepository.save(wish);
        Assertions.assertThat(wishUpdated.getWishName()).isEqualTo("Phone");
    }

    @Test
    @Order(5)
    void deleteWishById() {
        Wish wish = wishRepository.findById(1L).get();
        wishRepository.delete(wish);
        Wish wish1 = null;
        Optional<Wish> optionalWish = wishRepository.findById(1L);
        if (optionalWish.isPresent()) {
            wish1 = optionalWish.get();
        }
        Assertions.assertThat(wish1).isNull();
    }
}