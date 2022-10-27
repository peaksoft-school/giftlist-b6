package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.BookedService;
import kg.peaksoft.giftlistb6.dto.responses.GiftResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.dto.responses.BookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Booking Api", description = "Book wishes and copy other wishes.")
@RequestMapping("/api/bookings")
public class BookedApi {

    private final BookedService bookedService;


    @Operation(summary = "Book wishes",description = "User can book wish")
    @PostMapping("{id}")
    public SimpleResponse reserve(@PathVariable Long id,
                                  @RequestParam Boolean isAnonymous) {
        return bookedService.reserveWish(id, isAnonymous);
    }

    @Operation(summary = "All book wishes",description = "List of all canceled wishes")
    @GetMapping("/wishes")
    public List<BookResponse> getAll() {
        return bookedService.getAllReservedWishes();
    }

    @Operation(summary = "All books",description = "List of all gifts that I want to give")
    @GetMapping
    public List<GiftResponse> getAllGifts() {
        return bookedService.getAllGifts();
    }

    @Operation(summary = "Un reserve",description = "Un reserve wish")
    @PostMapping("/unReserve/{id}")
    public SimpleResponse unReserve(@PathVariable Long id) {
        return bookedService.waitStatus(id);
    }

    @Operation(summary = "Add friend Wish to my wish",description = "If I want the same gift for myself as another user, the gift card will be copied to the “My wishes” section, while the holiday of the gift will be copied to the “My holidays” section")
    @PostMapping("add/{id}")
    public SimpleResponse addFriendWishToMyWish(@PathVariable Long id) {
        return bookedService.saveWish(id);
    }
}
