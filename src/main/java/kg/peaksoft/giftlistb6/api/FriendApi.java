package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.FriendService;
import kg.peaksoft.giftlistb6.dto.responses.FriendInfoResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Friends Api", description = "User can send request,add and delete friends")
@RequestMapping("api/friends")
public class FriendApi {

    private final FriendService friendService;

    @Operation(summary = "Get all friends", description = "User can see get all friends list")
    @GetMapping
    public List<FriendInfoResponse> getAllFriends() {
        return friendService.getAllFriends();
    }

    @Operation(summary = "Get all requests", description = "User can see get all requests")
    @GetMapping("/requests")
    public List<FriendInfoResponse> getAllRequests() {
        return friendService.getAllRequests();
    }

    @Operation(summary = "Send request to friend", description = "User can send request to friend")
    @PostMapping("/request/{id}")
    public SimpleResponse requestToFriend(@PathVariable Long id) {
        return friendService.sendRequestToFriend(id);
    }

    @Operation(summary = "Reject request to friend", description = "User can reject request to friend")
    @PostMapping("/reject/{id}")
    public SimpleResponse reject(@PathVariable Long id) {
        return friendService.rejectRequest(id);
    }

    @Operation(summary = "Accept request to friend", description = "User can accept request to friend")
    @PostMapping("/accept/{id}")
    public SimpleResponse accept(@PathVariable Long id) {
        return friendService.acceptRequest(id);
    }

    @Operation(summary = "Delete from friend", description = "User can delete from friends")
    @DeleteMapping("/{id}")
    public SimpleResponse deleteFromFriend(@PathVariable Long id) {
        return friendService.deleteFromFriends(id);
    }

    @Operation(summary = "Cancel request to friend", description = "User can cancel request to friend")
    @PostMapping("/cancel/{id}")
    public SimpleResponse cancelRequestToFriend(@PathVariable Long id) {
        return friendService.cancelRequestToFriend(id);
    }
}
