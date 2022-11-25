package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.CharityService;
import kg.peaksoft.giftlistb6.dto.requests.CharityRequest;
import kg.peaksoft.giftlistb6.dto.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/charities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Charity Api", description = "Charity CRUD operations")
public class CharityApi {

    private final CharityService charityService;

    @Operation(summary = "All charities", description = "User can see all charities")
    @GetMapping
    public CharityResponses getAllCharities() {
        return charityService.getAllCharityResponse();
    }

    @Operation(summary = "Save charity", description = "User can save charities")
    @PostMapping
    public YourCharityResponse saveCharity(@RequestBody CharityRequest request) {
        return charityService.saveCharity(request);
    }

    @Operation(summary = "Update charity", description = "User can update charity")
    @PutMapping("{id}")
    public InnerPageCharityResponse updateCharity(@PathVariable Long id,
                                                  @RequestBody CharityRequest request) {
        return charityService.updateCharity(id, request);
    }

    @Operation(summary = "Get charity", description = "Get charity by id")
    @GetMapping("{id}")
    public InnerCharityResponse getCharityById(@PathVariable Long id) {
        return charityService.getCharityById(id);
    }

    @Operation(summary = "Delete charity", description = "User can delete own charity")
    @DeleteMapping("{id}")
    public SimpleResponse deleteCharityById(@PathVariable Long id) {
        return charityService.deleteCharityById(id);
    }

    @Operation(summary = "Reserve charity ",description = "User can reserve charity")
    @PostMapping("reservation/{id}")
    public SimpleResponse reserve(@PathVariable Long id,
                                  @RequestParam Boolean isAnonymously){
        return charityService.reserveCharity(id,isAnonymously);
    }

    @Operation(summary = "Unreserved charity ",description = "User can unreserved charity")
    @PostMapping("un-reservation/{id}")
    public SimpleResponse unReserve(@PathVariable Long id){
        return charityService.unReserve(id);
    }
}