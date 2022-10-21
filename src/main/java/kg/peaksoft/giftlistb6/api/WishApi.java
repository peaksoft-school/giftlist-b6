package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.services.WishService;
import kg.peaksoft.giftlistb6.dto.requests.WishRequest;
import kg.peaksoft.giftlistb6.dto.responses.InnerWishResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.dto.responses.WishResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish-list")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Wish Api", description = "Wish CRUD")
public class WishApi {

    private final WishService wishService;

    @Operation(summary = "Save wish", description = "User can save wish")
    @PostMapping
    public WishResponse saveWish(@RequestBody WishRequest wishRequest) {
        return wishService.saveWish(wishRequest);
    }


    @Operation(summary = "Update wish", description = "User can update information only own wish")
    @PutMapping("/{id}")
    public WishResponse updateWishById(@PathVariable Long id,
                                       @RequestBody WishRequest wishRequest) {
        return wishService.update(id, wishRequest);
    }

    @Operation(summary = "Delete wish", description = "User can delete wishlist, when we delete wish holiday and user will not be deleted")
    @DeleteMapping("/{id}")
    public SimpleResponse deleteWishById(@PathVariable Long id) {
        return wishService.deleteWishById(id);
    }


    @Operation(summary = "Get wish", description = "User can get wish by id")
    @GetMapping("/{id}")
    public InnerWishResponse getById(@PathVariable Long id) {
        return wishService.findById(id);
    }


    @Operation(summary = "Get all wishes", description = "User can get all wishes")
    @GetMapping
    public List<WishResponse> getAllWishes() {
        return wishService.findAll();
    }


    @Operation(summary = "Principal", description = "Only user gives")
    @GetMapping("/principal")
    public User getAuthPrincipal() {
        return wishService.getAuthPrincipal();
    }
}