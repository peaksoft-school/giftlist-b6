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
    @PostMapping("reserve/{id}")
    public SimpleResponse reservation(@PathVariable Long id,
                                      @RequestParam Boolean isAnonymous) {
        return bookedService.reserveWish(id, isAnonymous);
    }

    @Operation(summary = "Get all booked wishes",description = "User can get own booked wishes.")
    @GetMapping("wishes")
    public List<BookResponse> getAllWishes() {
        return bookedService.getAllReservedWishes();
    }

    @Operation(summary = "Get all booked gifts",description = "User can get own booked gifts.")
    @GetMapping
    public List<GiftResponse> getAllGifts() {
        return bookedService.getAllGifts();
    }

    @Operation(summary = "unreserved",description = "unreserved wish")
    @PostMapping("un-reservation/{id}")
    public SimpleResponse unReserve(@PathVariable Long id) {
        return bookedService.waitStatus(id);
    }

    @Operation(summary = "Add friend's wish to my wish",description = "User can add friend's wish to own wish")
    @PostMapping("/{id}")
    public SimpleResponse addFriendWishToMyWish(@PathVariable Long id) {
        return bookedService.saveWish(id);
    }
}
