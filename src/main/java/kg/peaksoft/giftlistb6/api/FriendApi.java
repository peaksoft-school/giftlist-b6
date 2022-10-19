package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.service.FriendService;
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
@Tag(name = "Friend Api", description = "Friends")
@RequestMapping("/friends")
public class FriendApi {

    private final FriendService friendService;

    @Operation(summary = "Get all friends", description = "Get all friends by authentication(token) user")
    @GetMapping("/getAllFriends")
    public List<FriendInfoResponse> getAllFriends() {
        return friendService.getAllFriends();
    }

    @Operation(summary = "Get all requests", description = "Get all requests by authentication(token) user")
    @GetMapping("/getAllRequests")
    public List<FriendInfoResponse> getAllRequests() {
        return friendService.getAllRequests();
    }

    @Operation(summary = "Send request to friend", description = "Send request to friend by friend id and by authentication(token) user")
    @PostMapping("/request/{friendId}")
    public SimpleResponse requestToFriend(@PathVariable Long friendId) {
        return friendService.sendRequestToFriend(friendId);
    }

    @Operation(summary = "Reject request to friend", description = "Reject request to friend by sender user id and by authentication(token) user")
    @PostMapping("/reject/{senderId}")
    public SimpleResponse reject(@PathVariable Long senderId) {
        return friendService.rejectRequest(senderId);
    }

    @Operation(summary = "Accept request to friend", description = "Accept request to friend by sender user id and by authentication(token) user")
    @PostMapping("/accept/{senderId}")
    public SimpleResponse accept(@PathVariable Long senderId) {
        return friendService.acceptRequest(senderId);
    }

    @Operation(summary = "Delete from friend", description = "Delete from friend by friend id and by authentication(token) user")
    @DeleteMapping("/delete/{friendId}")
    public SimpleResponse deleteFromFriend(@PathVariable Long friendId) {
        return friendService.deleteFromFriends(friendId);
    }

    @Operation(summary = "Cancel request to friend", description = "Cancel request to friend by friend id and by authentication(token) user")
    @PostMapping("/cancelRequest/{friendId}")
    public SimpleResponse cancelRequestToFriend(@PathVariable Long friendId) {
        return friendService.cancelRequestToFriend(friendId);
    }
}
