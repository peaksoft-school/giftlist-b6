package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Holiday;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.responses.WishResponse;
import kg.peaksoft.giftlistb6.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @InjectMocks
    private WishService wishService;

    @BeforeEach
    void setUp() {
        log.info("Before each: ");
    }

    @AfterEach
    void tearDown() throws Exception {
        log.info("after each!");
    }

    @Test
    void saveWish() {
        Wish wish = Wish.builder()
                .id(1L)
                .wishName("")
                .holiday(new Holiday())
                .dateOfHoliday(LocalDate.of(2022, 11, 18))
                .wishStatus(Status.RESERVED)
                .image("")
                .description("")
                .linkToGift("http://link")
                .complaints(new ArrayList<>())
                .isBlock(true)
                .reservoir(new User())
                .build();
        WishResponse wishes = wishService.mapToResponse(wish);
        assertAll(() -> assertEquals(wishes.getWishName(), wish.getWishName()));
    }

    @Test
    @Sql(scripts = "/scripts/wishes.sql")
    void findById() {
        Wish wish = wishRepository.findById(1L).get();
        assertEquals(wish.getId(), 1L);
    }

    @Test
    @Sql(scripts = "/scripts/wishes.sql")
    void findAll() {
        List<WishResponse> all = wishService.findAll();
        assertEquals(Collections.emptyList(), all);
        assertNotNull(wishService);
    }

    @Test
    @Sql(scripts = "/scripts/wishes.sql")
    void update() {
        Wish wish = wishRepository.findById(1L).get();
        wish.setWishName("Phone");
        Wish savedWish = wishRepository.save(wish);
        log.info("wish name = {}", savedWish.getWishName());
        assertEquals(
                savedWish.getWishName(),
                "Phone"
        );
    }

    @Test
    @Sql(scripts = "/scripts/wishes.sql")
    void deleteWishById() {
        int before = wishRepository.findAll().size();
        Wish wish = wishRepository.findById(1L).get();
        wishRepository.delete(wish);
        int after = wishRepository.findAll().size();
        assertThat(before).isGreaterThan(after);
    }
}