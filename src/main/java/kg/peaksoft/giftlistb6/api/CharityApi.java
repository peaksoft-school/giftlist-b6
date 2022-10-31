package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.CharityService;
import kg.peaksoft.giftlistb6.dto.requests.CharityRequest;
import kg.peaksoft.giftlistb6.dto.responses.CharityResponses;
import kg.peaksoft.giftlistb6.dto.responses.InnerPageCharityResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.dto.responses.YourCharityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/charities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Charity Api", description = "Charity CRUD operations")
public class CharityApi {

    private final CharityService charityService;

    @GetMapping
    @Operation(summary = "All charities", description = "User can see all charities")
    public CharityResponses getAllCharities() {
        return charityService.getAllCharityResponse();
    }

    @PostMapping
    @Operation(summary = "Save Charity", description = "User can save charities")
    public YourCharityResponse saveCharity(@RequestBody CharityRequest request) {
        return charityService.saveCharity(request);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update Charity", description = "User can update charity")
    public InnerPageCharityResponse updateCharity(@PathVariable Long id,
                                                  @RequestBody CharityRequest request) {
        return charityService.updateCharity(id, request);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Charity", description = "Get Charity By Id")
    public InnerPageCharityResponse getCharityById(@PathVariable Long id) {
        return charityService.findById(id);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete Charity", description = "User can delete own charity")
    public SimpleResponse deleteCharityById(@PathVariable Long id) {
        return charityService.deleteCharityById(id);
    }

}
