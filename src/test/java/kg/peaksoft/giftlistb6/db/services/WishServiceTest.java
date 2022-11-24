package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.api.WishApi;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.responses.WishResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

;

//@DataJpaTest
@Slf4j
@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @InjectMocks
    private WishService wishService;
    @Mock
    private WishRepository wishRepository;

//    public WishServiceTest() {
//        this.wishRepository = mock(WishRepository.class);
//        this.wishService = new WishService(wishRepository);
//    }

    @LocalServerPort
    private String port;

    private String url;

    @BeforeEach
    void setUp() {
        this.url = "http://localhost:" + port + "/api/wish-list";
    }

    @AfterEach
    void tearDown() {
        log.info("after each!");
    }


    @Test
    void saveWish() {

//        when(wishService.saveWish(wishResponse)).then(invocationOnMock ->
//                doThrow());
//        wishService.saveWish(wishResponse);
//        when(wishService.saveWish(null))
//                .thenReturn(new WishResponse())
//                .thenReturn(null);
//        WishResponse save = wishService.saveWish(null);
//        System.out.println(save);
    }

    @Test
    void update() {
    }

    @Test
    void deleteWishById() {
        when(wishRepository.existsById(1L)).thenReturn(true);
        assertThrows(NotFoundException.class, () -> {
            wishService.deleteWishById(1L);
        }, "Not exception");
//        assertNotNull(wishService);
    }

    @Test
    void findById() {
        doReturn(new WishResponse()).when(wishService).findById(anyLong());
        wishService.findById(anyLong());
    }

    @Test
    void findAll() {
        List<WishResponse> all = wishService.findAll();
        assertEquals(Collections.emptyList(), all);
        assertNotNull(wishService);
    }
}