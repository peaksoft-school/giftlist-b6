//package kg.peaksoft.giftlistb6.api;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import kg.peaksoft.giftlistb6.db.services.CharityService;
//import kg.peaksoft.giftlistb6.db.services.UserService;
//import kg.peaksoft.giftlistb6.dto.responses.SearchAllResponse;
//import kg.peaksoft.giftlistb6.dto.responses.SearchUserResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/search")
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RequiredArgsConstructor
//@Tag(name = "Search Api", description = "User can search other users and other charities")
//public class SearchApi {
//
//    private final UserService userService;
//    private final CharityService charityService;
//
//    @Operation(summary = "Search users", description = "User can search other users")
//    @GetMapping("/user")
//    public List<SearchUserResponse> searchUsers(@RequestParam(value = "text", required = false) String text) {
//
//        return userService.searchUser(text);
//    }
//
//    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
//    @Operation(summary = "Search charities", description = "User can search other charities")
//    @GetMapping("/charity")
//    public SearchAllResponse searchCharity(@RequestParam(value = "text", required = false) String text,
//                                           @RequestParam(value = "condition", required = false) String condition,
//                                           @RequestParam(value = "category", required = false) String category,
//                                           @RequestParam(value = "subCategory", required = false) String subCategory){
//        return charityService.searchCharity(text, condition, category, subCategory);
//    }
//}