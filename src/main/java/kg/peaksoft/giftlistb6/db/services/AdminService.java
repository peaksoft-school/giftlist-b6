package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Charity;
import kg.peaksoft.giftlistb6.db.models.Holiday;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.enums.Status;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final UserService userService;

    public List<AdminResponse> getAllUsers() {
        List<User> users = userRepository.getAll();
        List<AdminResponse> userList = new ArrayList<>();
        for (User u : users) {
            userList.add(userService.createUser(u));
        }
        return userList;
    }

    public FriendProfileResponse getUserById(Long id){
        FriendProfileResponse friendProfileResponse = new FriendProfileResponse();
        User friend = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким  id: %s не найден!", id))
        );
        friendProfileResponse.setId(friend.getId());
        friendProfileResponse.setEmail(friend.getEmail());
        friendProfileResponse.setFirstName(friend.getFirstName());
        friendProfileResponse.setLastName(friend.getLastName());
        friendProfileResponse.setPhoto(friend.getImage());
        friendProfileResponse.setIsBlock(friend.getIsBlock());
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
        for (Holiday holiday : friend.getHolidays()) {
            HolidayResponses holidayResponse = new HolidayResponses(
                    holiday.getId(),
                    holiday.getName(),
                    holiday.getDateOfHoliday(),
                    holiday.getImage(),
                    holiday.getIsBlock());
            holidayResponses.add(holidayResponse);
        }
        friendProfileResponse.setHolidayResponses(holidayResponses);
        List<CharityResponse> charityResponses = new ArrayList<>();
        for (Charity c : friend.getCharities()) {
            CharityResponse charityResponse = new CharityResponse();
            charityResponse.setId(c.getId());
            charityResponse.setName(c.getName());
            charityResponse.setCondition(c.getCondition());
            charityResponse.setImage(c.getImage());
            charityResponse.setCreatedDate(c.getCreatedAt());
            charityResponse.setDescription(c.getDescription());
            charityResponse.setCharityStatus(c.getCharityStatus());
            charityResponse.setIsBlock(c.getIsBlock());
            if (c.getReservoir() != null) {
                charityResponse.setReservedUserResponse(new ReservedUserResponse(c.getReservoir().getId(), c.getReservoir().getFirstName() + " " + c.getReservoir().getLastName(), c.getReservoir().getImage()));
            } else {
                charityResponse.setReservedUserResponse(new ReservedUserResponse());
            }
            charityResponses.add(charityResponse);
        }
        friendProfileResponse.setCharityResponses(charityResponses);

        List<FriendWishesResponse> wishResponses = new ArrayList<>();
        for (Wish w : friend.getWishes()) {
            FriendWishesResponse friendWishesResponse = new FriendWishesResponse();
            friendWishesResponse.setId(w.getId());
            friendWishesResponse.setWishName(w.getWishName());
            friendWishesResponse.setWishStatus(w.getWishStatus());
            friendWishesResponse.setDateOfHoliday(w.getDateOfHoliday());
            friendWishesResponse.setHolidayName(w.getHoliday().getName());
            friendWishesResponse.setDescription(w.getDescription());
            friendWishesResponse.setLinkToGift(w.getLinkToGift());
            friendWishesResponse.setImage(w.getImage());
            friendWishesResponse.setIsBlock(w.getIsBlock());
            if (w.getReservoir() != null) {
                friendWishesResponse.setReservedUserResponse(new ReservedUserResponse(w.getReservoir().getId(), w.getReservoir().getFirstName() + " " + w.getReservoir().getLastName(), w.getReservoir().getImage()));
            } else {
                friendWishesResponse.setReservedUserResponse(new ReservedUserResponse());
            }
            wishResponses.add(friendWishesResponse);
        }
        friendProfileResponse.setWishResponses(wishResponses);

        return friendProfileResponse;
    }

    @Transactional
    public SimpleResponse block(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User with id:{} not found", id);
            throw new NotFoundException("Пользователь с таким id: %s не найден");
        });
        user.setIsBlock(true);
        log.info("User with id:{} is blocked", id);
        return new SimpleResponse("Заблокирован", "Пользователь заблокирован");
    }

    @Transactional
    public SimpleResponse unBlock(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User with id:{} not found", id);
            throw new NotFoundException(
                    "Пользователь с таким id: %s не найден");
        });
        user.setIsBlock(false);
        log.info("User with id:{} is unblocked ", id);
        return new SimpleResponse("Разблокирован", "Пользователь разблокирован");
    }
}