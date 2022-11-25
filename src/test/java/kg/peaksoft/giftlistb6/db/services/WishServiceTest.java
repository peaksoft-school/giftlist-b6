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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @InjectMocks
    private WishService wishService;

    @LocalServerPort
    private String port;

    private String url;

    @BeforeEach
    void setUp() {
        this.url = "http://localhost:" + port + "/api/wish-list";
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
        Wish wish = wishRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Не найден"));
        assertEquals(wish.getId(), 1L);
//        doReturn(new WishResponse()).when(wishService).findById(anyLong());
//        wishService.findById(anyLong());
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
        Wish wish = wishRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Не найден"));
        wish.setWishName("Phone");
        Wish savedWish = wishRepository.save(wish);
        log.info("wish name = {}", savedWish.getWishName());
        assertEquals(
                savedWish.getWishName(),
                "Phone"
        );
    }

    @Test
    void deleteWishById() {
        Wish wish = Wish.builder().build();
        Mockito.doReturn(true).when(wishRepository).delete(wish);
//        SimpleResponse deleteResult= wishService.deleteWishById(anyLong());
//        System.out.println(wishService.deleteWishById(1L));
//        System.out.println(wishService.deleteWishById(2L));
//        Mockito.verify(wishRepository, Mockito.times(3)).delete(wish);
//        assertThat(wish.getWishName()).isEqualTo(wish.getId());
//        assertThat(deleteResult).isNull();
//        when(wishRepository.existsById(1L)).thenReturn(true);
//        assertThrows(NotFoundException.class, () -> {
//            wishService.deleteWishById(1L);
//        }, "Не найден");
//        assertNotNull(wishService);
    }

    @Test
    void throwExceptionIfDatabase() {
        Wish wish = Wish.builder().build();
        Mockito.doThrow(RuntimeException.class).when(wishRepository).delete(wish);
        assertThrows(RuntimeException.class, () -> wishService.deleteWishById(1L));
    }
}