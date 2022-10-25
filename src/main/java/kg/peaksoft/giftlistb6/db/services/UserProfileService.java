package kg.peaksoft.giftlistb6.db.service;

import kg.peaksoft.giftlistb6.db.model.*;
import kg.peaksoft.giftlistb6.db.repository.UserProfileRepository;
import kg.peaksoft.giftlistb6.db.repository.UserRepository;
import kg.peaksoft.giftlistb6.dto.requests.ProfileRequest;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository repository;
    private final UserRepository userRepository;

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("user with email %s not found",email)));
    }

    public ProfileResponse saveProfile(ProfileRequest request) {
        UserInfo userInfo = convertToEntity(request);
        return convertToResponse(userInfo);
    }

    public UserInfo updateUser(UserInfo userInfo, ProfileRequest request) {
        userInfo.setPhoto(request.getPhoto());
        userInfo.setCountry(request.getCountry());
        userInfo.setDateOfBirth(request.getDateOfBirth());
        userInfo.setPhoneNumber(request.getPhoneNumber());
        userInfo.setHobby(request.getHobby());
        userInfo.setImportant(request.getImportant());
        userInfo.setShoeSize(request.getShoeSize());
        userInfo.setClothingSize(request.getClothingSize());
        return userInfo;

    }

    public ProfileResponse saveUpdateUser(Long id, ProfileRequest request) {
        UserInfo userInfo = repository.findById(id).get();
        UserInfo userInfo1 = updateUser(userInfo,request);
        return convertToResponse(repository.save(userInfo1));
    }

    @Transactional
    public UserInfo convertToEntity(ProfileRequest request) {
        User user = getAuthPrincipal();
        UserInfo userInfo = new UserInfo();
        userInfo.setPhoto(request.getPhoto());
        userInfo.setCountry(request.getCountry());
        userInfo.setDateOfBirth(request.getDateOfBirth());
        userInfo.setPhoneNumber(request.getPhoneNumber());
        userInfo.setHobby(request.getHobby());
        userInfo.setImportant(request.getImportant());
        userInfo.setShoeSize(request.getShoeSize());
        userInfo.setClothingSize(request.getClothingSize());
        user.setUserInfo(userInfo);
        repository.save(userInfo);
        return userInfo;
    }

    @Transactional
    public ProfileResponse convertToResponse(UserInfo userInfo) {
        ProfileResponse response = new ProfileResponse();
        response.setId(userInfo.getId());
        response.setCountry(userInfo.getCountry());
        response.setClothingSize(userInfo.getClothingSize());
        response.setHobby(userInfo.getHobby());
        response.setImportant(userInfo.getImportant());
        response.setPhoto(userInfo.getPhoto());
        response.setPhoneNumber(userInfo.getPhoneNumber());
        response.setShoeSize(userInfo.getShoeSize());
        response.setDateOfBirth(userInfo.getDateOfBirth());
        return response;
    }

    public FriendProfileResponse friendProfile(Long id){
        FriendProfileResponse friendProfileResponse = new FriendProfileResponse();
        User user  = userRepository.findById(id).orElseThrow(
                ()-> new NotFoundException(String.format("user with id %s not found",id))
        );
        friendProfileResponse.setId(user.getId());
        friendProfileResponse.setPhoto(user.getPhoto());
        friendProfileResponse.setPhoneNumber(user.getUserInfo().getPhoneNumber());
        friendProfileResponse.setCountry(user.getUserInfo().getCountry());
        friendProfileResponse.setClothingSize(user.getUserInfo().getClothingSize());
        friendProfileResponse.setHobby(user.getUserInfo().getHobby());
        friendProfileResponse.setImportant(user.getUserInfo().getImportant());
        friendProfileResponse.setShoeSize(user.getUserInfo().getShoeSize());
        friendProfileResponse.setDateOfBirth(user.getUserInfo().getDateOfBirth());
        List<WishResponse> wishResponses = new ArrayList<>();
        for (Wish wish : user.getWishes()) {
            WishResponse wishResponse = new WishResponse(
                    wish.getId(),
                    wish.getWishName(),
                    wish.getLinkToGift(),
                    wish.getDateOfHoliday(),
                    wish.getDescription(),
                    wish.getImage(),
                    wish.getWishStatus());
            wishResponses.add(wishResponse);
        }

        List<HolidayResponse> holidayResponses = new ArrayList<>();
        for (Holiday holiday : user.getHolidays()) {
            HolidayResponse holidayResponse = new HolidayResponse(
                    holiday.getId(),
                    holiday.getName(),
                    holiday.getDateOfHoliday(),
                    holiday.getImage());
            holidayResponses.add(holidayResponse);
        }

        List<CharityResponse> charityResponses = new ArrayList<>();
        for (Charity charity : user.getCharities()) {
            CharityResponse charityResponse = new CharityResponse(
                    charity.getId(),
                    charity.getName(),
                    charity.getCharityStatus(),
                    charity.getDescription(),
                    charity.getCondition(),
                    charity.getImage(),
                    charity.getCreatedDate());
            charityResponses.add(charityResponse);
        }
        friendProfileResponse.setWishResponses(wishResponses);
        friendProfileResponse.setHolidayResponses(holidayResponses);
        friendProfileResponse.setCharityResponses(charityResponses);

        return friendProfileResponse;
    }

//    public FriendProfileResponse friendProfileResponse(User user ) {
//        UserInfo userInfo = new UserInfo();
//        FriendProfileResponse friendProfileResponse = new FriendProfileResponse();
//        friendProfileResponse.setId(userInfo.getId());
//        friendProfileResponse.setCountry(userInfo.getCountry());
//        friendProfileResponse.setClothingSize(userInfo.getClothingSize());
//        friendProfileResponse.setHobby(userInfo.getHobby());
//        friendProfileResponse.setImportant(userInfo.getImportant());
//        friendProfileResponse.setPhoto(userInfo.getPhoto());
//        friendProfileResponse.setPhoneNumber(userInfo.getPhoneNumber());
//        friendProfileResponse.setShoeSize(userInfo.getShoeSize());
//        friendProfileResponse.setDateOfBirth(userInfo.getDateOfBirth());
//        List<WishResponse> wishResponses = new ArrayList<>();
//        for (Wish wish : user.getWishes()) {
//            WishResponse wishResponse = new WishResponse(
//                    wish.getId(),
//                    wish.getWishName(),
//                    wish.getLinkToGift(),
//                    wish.getDateOfHoliday(),
//                    wish.getDescription(),
//                    wish.getImage(),
//                    wish.getWishStatus());
//            wishResponses.add(wishResponse);
//        }
//
//        List<HolidayResponse> holidayResponses = new ArrayList<>();
//        for (Holiday holiday : user.getHolidays()) {
//            HolidayResponse holidayResponse = new HolidayResponse(
//                    holiday.getId(),
//                    holiday.getName(),
//                    holiday.getDateOfHoliday(),
//                    holiday.getImage());
//            holidayResponses.add(holidayResponse);
//        }
//
//        List<CharityResponse> charityResponses = new ArrayList<>();
//        for (Charity charity : user.getCharities()) {
//            CharityResponse charityResponse = new CharityResponse(
//                    charity.getId(),
//                    charity.getName(),
//                    charity.getCharityStatus(),
//                    charity.getDescription(),
//                    charity.getCondition(),
//                    charity.getImage(),
//                    charity.getCreatedDate());
//            charityResponses.add(charityResponse);
//        }
//
//        friendProfileResponse.setWishResponses(wishResponses);
//        friendProfileResponse.setHolidayResponses(holidayResponses);
//        friendProfileResponse.setCharityResponses(charityResponses);
//        return friendProfileResponse;
//    }

}