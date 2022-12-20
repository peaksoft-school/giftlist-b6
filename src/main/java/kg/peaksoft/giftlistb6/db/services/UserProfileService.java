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

    @Transactional
    public UserInfo updateUser(UserInfo userInfo, ProfileRequest request) {
        User user = getAuthPrincipal();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setImage(request.getImage());
        userInfo.setCountry(request.getCountry());
        userInfo.setDateOfBirth(request.getDateOfBirth());
        userInfo.setPhoneNumber(request.getPhoneNumber());
        userInfo.setHobby(request.getHobby());
        userInfo.setImportant(request.getImportant());
        userInfo.setShoeSize(request.getShoeSize());
        userInfo.setClothingSize(request.getClothingSize());
        user.setUserInfo(userInfo);
        userInfo.setUser(user);
        userInfo.setFacebookLink(request.getFacebookLink());
        userInfo.setInstagramLink(request.getInstagramLink());
        userInfo.setTelegramLink(request.getTelegramLink());
        userInfo.setVkLink(request.getVkLink());
        return userInfo;
    }

    @Transactional
    public ProfileResponse saveUpdateUser(Long id, ProfileRequest request) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователь с" + id + " не найден!")
        );
        UserInfo userInfo1 = updateUser(user.getUserInfo(), request);
        log.info("User with id: {} successfully updated", user.getId());
        return convertToResponse(repository.save(userInfo1));
    }

    @Transactional
    public UserInfo convertToEntity(ProfileRequest request) {
        User user = getAuthPrincipal();
        UserInfo userInfo = new UserInfo();
        user.setImage(request.getImage());
        userInfo.setCountry(request.getCountry());
        userInfo.setDateOfBirth(request.getDateOfBirth());
        userInfo.setPhoneNumber(request.getPhoneNumber());
        userInfo.setHobby(request.getHobby());
        userInfo.setImportant(request.getImportant());
        userInfo.setShoeSize(request.getShoeSize());
        userInfo.setClothingSize(request.getClothingSize());
        user.setUserInfo(userInfo);
        userInfo.setUser(user);
        userInfo.setFacebookLink(request.getFacebookLink());
        userInfo.setInstagramLink(request.getInstagramLink());
        userInfo.setTelegramLink(request.getTelegramLink());
        userInfo.setVkLink(request.getVkLink());
        repository.save(userInfo);
        return userInfo;
    }

    @Transactional
    public ProfileResponse convertToResponse(UserInfo userInfo) {
        ProfileResponse response = new ProfileResponse();
        User user = getAuthPrincipal();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCountry(userInfo.getCountry());
        response.setClothingSize(userInfo.getClothingSize());
        response.setHobby(userInfo.getHobby());
        response.setImportant(userInfo.getImportant());
        response.setImage(user.getImage());
        response.setPhoneNumber(userInfo.getPhoneNumber());
        response.setShoeSize(userInfo.getShoeSize());
        response.setDateOfBirth(userInfo.getDateOfBirth());
        response.setFacebookLink(userInfo.getFacebookLink());
        response.setInstagramLink(userInfo.getInstagramLink());
        response.setTelegramLink(userInfo.getTelegramLink());
        response.setVkLink(userInfo.getVkLink());
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
        ProfileResponse response = new ProfileResponse();
        User user = getAuthPrincipal();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCountry(user.getUserInfo().getCountry());
        response.setClothingSize(user.getUserInfo().getClothingSize());
        response.setHobby(user.getUserInfo().getHobby());
        response.setImportant(user.getUserInfo().getImportant());
        response.setImage(user.getImage());
        response.setPhoneNumber(user.getUserInfo().getPhoneNumber());
        response.setShoeSize(user.getUserInfo().getShoeSize());
        response.setDateOfBirth(user.getUserInfo().getDateOfBirth());
        response.setFacebookLink(user.getUserInfo().getFacebookLink());
        response.setInstagramLink(user.getUserInfo().getInstagramLink());
        response.setTelegramLink(user.getUserInfo().getTelegramLink());
        response.setVkLink(user.getUserInfo().getVkLink());
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
        friendProfileResponse.setFacebookLink(friend.getUserInfo().getFacebookLink());
        friendProfileResponse.setInstagramLink(friend.getUserInfo().getInstagramLink());
        friendProfileResponse.setTelegramLink(friend.getUserInfo().getTelegramLink());
        friendProfileResponse.setVkLink(friend.getUserInfo().getVkLink());

        List<HolidayResponses> holidayResponses = new ArrayList<>();
        List<Holiday>block = new ArrayList<>();
        for (Holiday holiday : friend.getHolidays()) {
            if (holiday.getIsBlock().equals(true)){
                block.add(holiday);
            }else {
            HolidayResponses holidayResponse = new HolidayResponses(
                    holiday.getId(),
                    holiday.getName(),
                    holiday.getDateOfHoliday(),
                    holiday.getImage());
            holidayResponses.add(holidayResponse);
            }
        }
        friendProfileResponse.setHolidayResponses(holidayResponses);
        List<CharityResponse> charityResponses = new ArrayList<>();
        List<Charity> blockCharities = new ArrayList<>();
        for (Charity c : friend.getCharities()) {
            if (c.getIsBlock().equals(true)){
                blockCharities.add(c);
            }else {
            CharityResponse charityResponse = new CharityResponse();
            charityResponse.setId(c.getId());
            charityResponse.setName(c.getName());
            charityResponse.setCondition(c.getCondition());
            charityResponse.setImage(c.getImage());
            charityResponse.setCreatedDate(c.getCreatedAt());
            charityResponse.setDescription(c.getDescription());
            charityResponse.setCharityStatus(c.getCharityStatus());
            if (c.getReservoir() != null && !c.getReservoir().equals(user) && c.getCharityStatus().equals(Status.RESERVED)) {
                charityResponse.setIsMy(false);
            } else if (c.getCharityStatus().equals(Status.WAIT)) {
                charityResponse.setIsMy(false);
            } else {
                charityResponse.setIsMy(true);
            }
            if (c.getReservoir() != null) {
                charityResponse.setReservedUserResponse(new ReservedUserResponse(c.getReservoir().getId(), c.getReservoir().getFirstName() + " " + c.getReservoir().getLastName(), c.getReservoir().getImage()));
            } else {
                charityResponse.setReservedUserResponse(new ReservedUserResponse());
            }
            charityResponses.add(charityResponse);
            }
        }
        friendProfileResponse.setCharityResponses(charityResponses);

        List<FriendWishesResponse> wishResponses = new ArrayList<>();
        List<Wish> blockWish = new ArrayList<>();
        for (Wish w : friend.getWishes()) {
            if (w.getIsBlock().equals(true)){
                blockWish.add(w);
            }else {
            FriendWishesResponse friendWishesResponse = new FriendWishesResponse();
            friendWishesResponse.setId(w.getId());
            friendWishesResponse.setWishName(w.getWishName());
            friendWishesResponse.setWishStatus(w.getWishStatus());
            friendWishesResponse.setDateOfHoliday(w.getDateOfHoliday());
            friendWishesResponse.setHolidayName(w.getHoliday().getName());
            friendWishesResponse.setDescription(w.getDescription());
            friendWishesResponse.setLinkToGift(w.getLinkToGift());
            friendWishesResponse.setImage(w.getImage());
            if (w.getReservoir() != null && !w.getReservoir().equals(user) && w.getWishStatus().equals(Status.RESERVED)) {
                friendWishesResponse.setIsMy(false);
            } else if (w.getWishStatus().equals(Status.WAIT)) {
                friendWishesResponse.setIsMy(false);
            } else {
                friendWishesResponse.setIsMy(true);
            }
            if (w.getReservoir() != null) {
                friendWishesResponse.setReservedUserResponse(new ReservedUserResponse(w.getReservoir().getId(), w.getReservoir().getFirstName() + " " + w.getReservoir().getLastName(), w.getReservoir().getImage()));
            } else {
                friendWishesResponse.setReservedUserResponse(new ReservedUserResponse());
            }
            wishResponses.add(friendWishesResponse);
            }
        }
        friendProfileResponse.setWishResponses(wishResponses);

        return friendProfileResponse;
    }
}