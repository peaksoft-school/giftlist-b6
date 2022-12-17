package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.*;
import kg.peaksoft.giftlistb6.dto.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Admin Api", description = "Admin can block,unblock and get users.")
@RequestMapping("/api/admin")
public class AdminApi {

    private final AdminService adminService;
    private final CharityService charityService;
    private final UserProfileService service;
    private final ComplaintsService complaintsService;
    private final HolidayService holidayService;
    private final WishService wishService;

    @Operation(summary = "Get all users", description = "Admin can see all users.")
    @GetMapping("/users")
    public List<AdminResponse> getAllUsers() {
        return adminService.getAllUsers();
    }

    @Operation(summary = "Get by id", description = "Admin see user profile")
    @GetMapping("profile/{id}")
    public FriendProfileResponse FriendProfile(@PathVariable Long id) {
        return adminService.getUserById(id);
    }

    @Operation(summary = "Block user", description = "Block user by id.")
    @PutMapping("/block/{id}")
    public SimpleResponse block(@PathVariable("id") Long id) {
        return adminService.block(id);
    }

    @Operation(summary = "Unblock user", description = "Unblock user by id.")
    @PutMapping("/unblock/{id}")
    public SimpleResponse unBlock(@PathVariable("id") Long id) {
        return adminService.unBlock(id);
    }

    @Operation(summary = "Get charity", description = "Get charity by id")
    @GetMapping("{id}")
    public InnerCharityResponse getCharityWithId(@PathVariable Long id) {
        return charityService.getCharityByIdWithAdmin(id);
    }

    @Operation(summary = "All charities", description = "Admin can see all charities")
    @GetMapping("/charities")
    public CharityResponses getAllCharities() {
        return charityService.getAllCharityResponseByAdmin();
    }

    @Operation(summary = "Block wish", description = "Admin can block user wishes")
    @PutMapping("wish-block/{id}")
    public SimpleResponse blockWishByIdFromComplaint(@PathVariable Long id) {
        return complaintsService.blockWishByIdFromComplaint(id);
    }

    @Operation(summary = "Unblock wish", description = "Admin can unblock user wishes")
    @PutMapping("wish-unblock{id}")
    public SimpleResponse unBlockWishByIdFromComplaint(@PathVariable Long id) {
        return complaintsService.unBlockWishByIdFromComplaint(id);
    }

    @Operation(summary = "Block holiday", description = "Admin can block user holidays")
    @PutMapping("holiday-block/{id}")
    public SimpleResponse blockHoliday(@PathVariable Long id) {
        return holidayService.blockHoliday(id);
    }

    @Operation(summary = "Unblock holiday", description = "Admin can unblock user holidays")
    @PutMapping("holiday-unblock{id}")
    public SimpleResponse unblockHoliday(@PathVariable Long id) {
        return holidayService.unblockHoliday(id);
    }

    @Operation(summary = "Block charity", description = "Admin can block user charity")
    @PutMapping("charity-block/{id}")
    public SimpleResponse blockCharity(@PathVariable Long id) {
        return charityService.blockCharity(id);
    }

    @Operation(summary = "UnBlock charity", description = "Admin can unblock user charity")
    @PutMapping("charity-unblock{id}")
    public SimpleResponse unblockCharity(@PathVariable Long id) {
        return charityService.unblockCharity(id);
    }

    @Operation(summary = "Delete charity", description = "Admin can delete user charity")
    @DeleteMapping("charity{id}")
    public SimpleResponse deleteCharityById(@PathVariable Long id) {
        return charityService.deleteCharityByAdmin(id);
    }

    @Operation(summary = "Delete holiday", description = "Admin can delete holiday by id")
    @DeleteMapping("holiday/{id}")
    public SimpleResponse deleteHolidayById(@PathVariable Long id) {
        return holidayService.deleteHolidayById(id);
    }

    @Operation(summary = "Delete wish", description = "Admin can delete wish")
    @DeleteMapping("wish/{id}")
    public SimpleResponse deleteWishById(@PathVariable Long id) {
        return wishService.deleteWish(id);
    }
}