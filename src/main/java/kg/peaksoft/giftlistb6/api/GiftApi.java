package kg.peaksoft.giftlistb6.api;

import kg.peaksoft.giftlistb6.db.services.BookedService;
import kg.peaksoft.giftlistb6.dto.responses.GiftResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.dto.responses.WishResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gifts")
public class GiftApi {

    private final BookedService bookedService;

    @PostMapping("{id}")
    public SimpleResponse reserve(@PathVariable Long id,
                                  @RequestParam Boolean isAnonymous) {
        return bookedService.reserveWish(id, isAnonymous);
    }

    @GetMapping("/wishes")
    public List<WishResponse> getAll() {
        return bookedService.getAllReservedWishes();
    }

    @GetMapping
    public List<GiftResponse> getAllGifts() {
        return bookedService.getAllGifts();
    }

    @PostMapping("/unReserve/{id}")
    public SimpleResponse unReserve(@PathVariable Long id) {
        return bookedService.waitStatus(id);
    }

    @PostMapping("add/{id}")
    public SimpleResponse addFriendWishToMyWish(@PathVariable Long id) {
        return bookedService.saveWish(id);
    }
}
