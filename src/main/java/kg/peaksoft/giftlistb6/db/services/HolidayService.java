package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Holiday;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.GiftRepository;
import kg.peaksoft.giftlistb6.db.repositories.HolidayRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.requests.HolidayRequest;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HolidayService {

    private final HolidayRepository holidayRepository;

    private final UserRepository userRepository;

    private final WishRepository repository;

    private final GiftRepository giftRepository;

    public User getPrinciple() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Holiday with id = %s not found", email))
        );
    }

    public HolidayResponses saveHoliday(HolidayRequest request) {
        Holiday holiday = convertToEntity(request);
        return convertToResponse(holidayRepository.save(holiday));
    }

    public HolidayResponseForGet getHolidayById(Long id) {
        Holiday holiday = holidayRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Holiday with id = %s not found", id)));
        return convertToResponseForGetById(holiday);
    }

    public SimpleResponse deleteHolidayById(Long id) {
        Holiday holiday = holidayRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Holiday with id = %s not found", id)));
        for (Wish w : holiday.getWishes()) {
            giftRepository.deleteByWishId(w.getId());
        }
        holidayRepository.deleteByWishId(holiday.getId());
        holiday.setUser(null);
        holidayRepository.delete(holiday);

        return new SimpleResponse(
                "DELETED",
                "holiday with id: " + holiday.getId() + " deleted successfully"
        );
    }

    public HolidayResponses saveUpdateHoliday(Long id, HolidayRequest request) {
        Holiday holiday = holidayRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Holiday with id = %s not found", id)));
        Holiday holiday1 = updateHoliday(holiday, request);
        return convertToResponse(holiday1);
    }

    public List<HolidayResponses> getAllHolidays() {
        User user = getPrinciple();
        return holidayRepository.getAllHolidays(user.getEmail());
    }

    public Holiday updateHoliday(Holiday holiday, HolidayRequest holidayRequest) {
        holiday.setName(holidayRequest.getName());
        holiday.setDateOfHoliday(holidayRequest.getDateOfHoliday());
        holiday.setImage(holidayRequest.getImage());
        return holidayRepository.save(holiday);
    }

    public Holiday convertToEntity(HolidayRequest request) {
        User user = getPrinciple();
        Holiday holiday = new Holiday();
        holiday.setUser(user);
        holiday.setName(request.getName());
        holiday.setDateOfHoliday(request.getDateOfHoliday());
        holiday.setImage(request.getImage());
        return holiday;
    }

    public HolidayResponseForGet convertToResponseForGetById(Holiday holiday) {
        List<WishResponses> wishResponses = new ArrayList<>();
        for (Wish wish : holiday.getWishes()) {
            WishResponses wishResponse = new WishResponses(wish.getId(), wish.getWishName(),
                    wish.getLinkToGift(), wish.getDateOfHoliday(), wish.getDescription(), wish.getImage(), wish.getWishStatus());
            wishResponses.add(wishResponse);
        }
        User user = getPrinciple();
        HolidayResponseForGet holidayResponse = new HolidayResponseForGet();
        holidayResponse.setId(holiday.getId());
        holidayResponse.setName(holiday.getName());
        holidayResponse.setDateOfHoliday(holiday.getDateOfHoliday());
        holidayResponse.setImage(holiday.getImage());
        holidayResponse.setUser(user.getId());
        holidayResponse.setWishResponse(wishResponses);
        return holidayResponse;
    }

    public HolidayResponses convertToResponse(Holiday holiday) {
        User user = getPrinciple();
        HolidayResponses holidayResponses = new HolidayResponses();
        holidayResponses.setId(holiday.getId());
        holidayResponses.setName(holiday.getName());
        holidayResponses.setDateOfHoliday(holiday.getDateOfHoliday());
        holidayResponses.setImage(holiday.getImage());
        holidayResponses.setUser(user.getId());
        return holidayResponses;
    }
}
