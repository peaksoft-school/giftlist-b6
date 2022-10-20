package kg.peaksoft.giftlistb6.api;

import kg.peaksoft.giftlistb6.db.service.UserProfileService;
import kg.peaksoft.giftlistb6.dto.requests.ProfileRequest;
import kg.peaksoft.giftlistb6.dto.responses.FriendProfileResponse;
import kg.peaksoft.giftlistb6.dto.responses.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/userInfo")
public class UserInfoApi {

    private final UserProfileService service;

    @PostMapping
    public ProfileResponse saveUserInfo(@RequestBody ProfileRequest request) {
        return service.saveProfile(request);
    }

    @PutMapping("{id}")
    public ProfileResponse updateProfileUser(@PathVariable Long id, @RequestBody ProfileRequest request) {
        return service.saveUpdateUser(id,request);
    }

    @GetMapping("{id}")
    public FriendProfileResponse showFriendProfile(@PathVariable Long id){
        return service.friendProfile(id);
    }
}
