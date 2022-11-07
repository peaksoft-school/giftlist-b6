package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.ComplaintsService;
import kg.peaksoft.giftlistb6.dto.requests.ComplaintRequest;
import kg.peaksoft.giftlistb6.dto.responses.ComplaintResponseForAdmin;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Complaints Api", description = "User can leave a complaint to the wishes of other users")
@PreAuthorize("hasAuthority('ADMIN')")
public class ComplaintsApi {

    private final ComplaintsService complaintsService;

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Create complaint",description = "User can created complaints")
    @PostMapping
    public SimpleResponse createComplain(@RequestBody ComplaintRequest request){
        return complaintsService.creatComplain(request);
    }

    @Operation(summary = "Get complaint",description = "Admin can get complaint by id")
    @GetMapping("{id}")
    public ComplaintResponseForAdmin getComplainBuyId(@PathVariable Long id){
        return complaintsService.getComplaintById(id);
    }

    @Operation(summary = "Get all complaints",description = "Admin can see all complaints")
    @GetMapping
    public List<ComplaintResponseForAdmin> getAllComplains(){
        return complaintsService.getAllComplaints();
    }

    @Operation(summary = "Block wish",description = "Admin can block user wish")
    @GetMapping("block/{id}")
    public SimpleResponse blockWishByIdFromComplaint(@PathVariable Long id){
        return complaintsService.blockWishByIdFromComplaint(id);
    }

    @Operation(summary = "Unblock wish",description = "Admin can unblock user wish")
    @PutMapping("/unblock/{id}")
    public SimpleResponse unBlockWishByIdFromComplaint(@PathVariable Long id){
        return complaintsService.unBlockWishByIdFromComplaint(id);
    }
}