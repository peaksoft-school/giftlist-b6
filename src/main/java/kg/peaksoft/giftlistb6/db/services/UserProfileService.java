package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.*;
import kg.peaksoft.giftlistb6.db.repositories.UserProfileRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.requests.ProfileRequest;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.enums.Status;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository repository;
    private final UserRepository userRepository;

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким  электронным адресом: %s не найден!", email)));
    }

    public ProfileResponse saveProfile(ProfileRequest request) {
        UserInfo userInfo = convertToEntity(request);
        log.info("User info saved in database");
        return convertToResponse(userInfo);
    }

    public UserInfo updateUser(UserInfo userInfo, ProfileRequest request) {
        userInfo.setImage(request.getImage());
        userInfo.setCountry(request.getCountry());
        userInfo.setDateOfBirth(request.getDateOfBirth());
        userInfo.setPhoneNumber(request.getPhoneNumber());
        userInfo.setHobby(request.getHobby());
        userInfo.setImportant(request.getImportant());
        userInfo.setShoeSize(request.getShoeSize());
        userInfo.setClothingSize(request.getClothingSize());
        return userInfo;
    }

    public ProfileResponse saveUpdateUser(ProfileRequest request) {
        User user = getAuthPrincipal();
        UserInfo userInfo1 = updateUser(user.getUserInfo(), request);
        log.info("User with id: {} successfully updated",user.getId());
        return convertToResponse(repository.save(userInfo1));
    }

    @Transactional
    public UserInfo convertToEntity(ProfileRequest request) {
        User user = getAuthPrincipal();
        UserInfo userInfo = new UserInfo();
        userInfo.setImage(request.getImage());
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
        User user = getAuthPrincipal();
        response.setId(userInfo.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCountry(userInfo.getCountry());
        response.setClothingSize(userInfo.getClothingSize());
        response.setHobby(userInfo.getHobby());
        response.setImportant(userInfo.getImportant());
        response.setImage(userInfo.getImage());
        response.setPhoneNumber(userInfo.getPhoneNumber());
        response.setShoeSize(userInfo.getShoeSize());
        response.setDateOfBirth(userInfo.getDateOfBirth());
        return response;
    }

    public MyProfileResponse myProfile() {
        User user = getAuthPrincipal();
        MyProfileResponse myProfileResponse = new MyProfileResponse();
        myProfileResponse.setId(user.getId());
        myProfileResponse.setFirstName(user.getFirstName());
        myProfileResponse.setLastName(user.getLastName());
        myProfileResponse.setImage(user.getImage());
        myProfileResponse.setEmail(user.getEmail());
        return myProfileResponse;
    }

    @Transactional
    public ProfileResponse getFullInfoMyProfile() {
        UserInfo userInfo = new UserInfo();
        ProfileResponse response = new ProfileResponse();
        User user = getAuthPrincipal();
        response.setId(user.getUserInfo().getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCountry(user.getUserInfo().getCountry());
        response.setClothingSize(user.getUserInfo().getClothingSize());
        response.setHobby(user.getUserInfo().getHobby());
        response.setImportant(user.getUserInfo().getImportant());
        response.setImage(user.getUserInfo().getImage());
        response.setPhoneNumber(user.getUserInfo().getPhoneNumber());
        response.setShoeSize(user.getUserInfo().getShoeSize());
        response.setDateOfBirth(user.getUserInfo().getDateOfBirth());
        return response;
    }

    public FriendProfileResponse friendProfile(Long id) {
        FriendProfileResponse friendProfileResponse = new FriendProfileResponse();
        User user = getAuthPrincipal();
        User friend = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким  id: %s не найден!", id))
        );
        friendProfileResponse.setId(friend.getId());
        friendProfileResponse.setEmail(friend.getEmail());
        if (user.getFriends().contains(friend) || friend.getFriends().contains(user)) {
            friendProfileResponse.setStatus(Status.FRIEND);
        } else if (user.getRequests().contains(friend) || friend.getRequests().contains(user)) {
            friendProfileResponse.setStatus(Status.REQUEST_TO_FRIEND);
        } else {
            friendProfileResponse.setStatus(Status.NOT_FRIEND);
        }
        friendProfileResponse.setFirstName(friend.getFirstName());
        friendProfileResponse.setLastName(friend.getLastName());
        friendProfileResponse.setPhoto(friend.getImage());
        friendProfileResponse.setPhoneNumber(friend.getUserInfo().getPhoneNumber());
        friendProfileResponse.setCountry(friend.getUserInfo().getCountry());
        friendProfileResponse.setClothingSize(friend.getUserInfo().getClothingSize());
        friendProfileResponse.setHobby(friend.getUserInfo().getHobby());
        friendProfileResponse.setImportant(friend.getUserInfo().getImportant());
        friendProfileResponse.setShoeSize(friend.getUserInfo().getShoeSize());
        friendProfileResponse.setDateOfBirth(friend.getUserInfo().getDateOfBirth());

        List<HolidayResponses> holidayResponses = new ArrayList<>();
        for (Holiday holiday : friend.getHolidays()) {
            HolidayResponses holidayResponse = new HolidayResponses(
                    holiday.getId(),
                    holiday.getName(),
                    holiday.getDateOfHoliday(),
                    holiday.getImage());
            holidayResponses.add(holidayResponse);
        }

        List<CharityResponse> charityResponses = new ArrayList<>();
        for (Charity charity : friend.getCharities()) {
            CharityResponse charityResponse = new CharityResponse(
                    charity.getId(),
                    charity.getName(),
                    charity.getCharityStatus(),
                    charity.getDescription(),
                    charity.getCondition(),
                    charity.getImage(),
                    charity.getCreatedAt());
            charityResponses.add(charityResponse);
        }
        friendProfileResponse.setHolidayResponses(holidayResponses);
        friendProfileResponse.setCharityResponses(charityResponses);

        List<HolidayGiftsResponse> wishResponses = new ArrayList<>();
        for (Wish wish : friend.getWishes()) {
            if (wish.getIsBlock().equals(false)) {
                HolidayGiftsResponse wishResponse = new HolidayGiftsResponse(
                        wish.getId(),
                        wish.getWishName(),
                        wish.getHoliday().getName(),
                        wish.getLinkToGift(),
                        wish.getDateOfHoliday(),
                        wish.getDescription(),
                        wish.getImage(),
                        wish.getWishStatus());
                wishResponses.add(wishResponse);
            }
        }
        friendProfileResponse.setWishResponses(wishResponses);

        return friendProfileResponse;
    }
}