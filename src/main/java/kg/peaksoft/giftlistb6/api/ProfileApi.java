package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.UserProfileService;
import kg.peaksoft.giftlistb6.dto.requests.ProfileRequest;
import kg.peaksoft.giftlistb6.dto.responses.FriendProfileResponse;
import kg.peaksoft.giftlistb6.dto.responses.ShowMyResponse;
import kg.peaksoft.giftlistb6.dto.responses.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/profile")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Profile api", description = "Can save,update profile")
@PreAuthorize("hasAuthority('USER')")
public class ProfileApi {

    private final UserProfileService service;

    @Operation(summary = "Save profile", description = "Can save profile  ")
    @PostMapping
    public ProfileResponse saveUserInfo(@RequestBody ProfileRequest request) {
        return service.saveProfile(request);
    }

    @Operation(summary = "Update profile", description = "Can update profile")
    @PutMapping("/{id}")
    public ProfileResponse updateProfileUser(@PathVariable Long id, @RequestBody ProfileRequest request) {
        return service.saveUpdateUser(id, request);
    }

    @Operation(summary = "Friends profile", description = "Can see friends profile")
    @GetMapping("{id}")
    public FriendProfileResponse FriendProfile(@PathVariable Long id) {
        return service.friendProfile(id);
    }

    @Operation(summary = "My profile", description = "Can see profile")
    @GetMapping("/me/{id}")
    public ShowMyResponse myProfile(@PathVariable Long id) {
        return service.myProfile(id);
    }
}
