package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.AdminCharityService;
import kg.peaksoft.giftlistb6.db.services.AdminService;
import kg.peaksoft.giftlistb6.dto.responses.AdminResponse;
import kg.peaksoft.giftlistb6.dto.responses.CharityResponses;
import kg.peaksoft.giftlistb6.dto.responses.InnerCharityResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
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
    private final AdminCharityService charityService;

    @Operation(summary = "Get all users", description = "Admin can see all users.")
    @GetMapping("/users")
    public List<AdminResponse> getAllUsers() {
        return adminService.getAllUsers();
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
        return charityService.getCharityById(id);
    }

    @Operation(summary = "All charities", description = "Admin can see all charities")
    @GetMapping
    public CharityResponses getAllCharities() {
        return charityService.getAllCharityResponse();
    }
}