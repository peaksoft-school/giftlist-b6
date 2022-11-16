package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.AdminCharityService;
import kg.peaksoft.giftlistb6.dto.responses.CharityResponses;
import kg.peaksoft.giftlistb6.dto.responses.InnerCharityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/charities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Admin charity Api", description = "Admin can see all charities ")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminCharityApi {

    private final AdminCharityService charityService;

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
