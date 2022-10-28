package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.UserProfileService;
import kg.peaksoft.giftlistb6.dto.requests.ProfileRequest;
import kg.peaksoft.giftlistb6.dto.responses.FriendProfileResponse;
import kg.peaksoft.giftlistb6.dto.responses.FriendShowResponse;
import kg.peaksoft.giftlistb6.dto.responses.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/profile")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "User profile api", description = "User can save,update profile")
@PreAuthorize("hasAuthority('USER')")
public class UserProfileApi {

    private final UserProfileService service;

    @PostMapping
    @Operation(summary = "Save profile", description = "user can save profile  ")
    public ProfileResponse saveUserInfo(@RequestBody ProfileRequest request)  {
        return service.saveProfile(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update profile", description = "user can update profile")
    public ProfileResponse updateProfileUser(@PathVariable Long id, @RequestBody ProfileRequest request) {
        return service.saveUpdateUser(id,request);
    }

    @GetMapping("{id}")
    @Operation(summary = "My friend profile", description = "user can see friend profile")
    public FriendProfileResponse FriendProfile(@PathVariable Long id){
        return service.friendProfile(id);
    }

    @GetMapping("/show/{id}")
    @Operation(summary = "My profile", description = "user can see profile")
    public FriendShowResponse myProfile(@PathVariable Long id) {
        return service.getProfile(id);
    }
}
