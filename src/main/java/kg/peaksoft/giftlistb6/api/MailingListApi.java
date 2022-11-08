package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.MailingListService;
import kg.peaksoft.giftlistb6.dto.requests.MailingListRequest;
import kg.peaksoft.giftlistb6.dto.responses.AllMailingListResponse;
import kg.peaksoft.giftlistb6.dto.responses.MailingListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Mailing list Api", description = "Admin can add mailing list")
@RequestMapping("api/mailing-list")
public class MailingListApi {

    private final MailingListService mailingListService;

    @Operation(summary = "Save mailing list", description = "Can save mailing list  ")
    @PostMapping
    public AllMailingListResponse saveUserInfo(@RequestBody MailingListRequest request) {
        return mailingListService.saveMailingList(request);
    }

    @Operation(summary = "Mailing list", description = "Get mailing list by id")
    @PostMapping("{id}")
    public MailingListResponse getById(@PathVariable Long id) {
        return mailingListService.getId(id);
    }

    @Operation(summary = "All mailing lists", description = "Show all mailing-list")
    @GetMapping
    public List<AllMailingListResponse> getAllMailingLists() {
        return mailingListService.getAllMailingLists();
    }
}