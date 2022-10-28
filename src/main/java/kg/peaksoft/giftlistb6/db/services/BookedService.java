package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Gift;
import kg.peaksoft.giftlistb6.db.models.Holiday;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.GiftRepository;
import kg.peaksoft.giftlistb6.db.repositories.HolidayRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.responses.GiftResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.dto.responses.BookResponse;
import kg.peaksoft.giftlistb6.enums.Status;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookedService {

    private final GiftRepository giftRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    private final HolidayRepository holidayRepository;

    public User getPrinciple() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Holiday with id = %s not found", email)));
    }

    @Transactional
    public SimpleResponse reserveWish(Long wishId, boolean is) {
        User user = getPrinciple();
        Wish wish = wishRepository.findById(wishId).get();
        if (wish.getWishStatus().equals(Status.WAIT)) {
            Gift gift = new Gift();
            gift.setWish(wish);
            if (is) {
                wish.setReservoir(null);
            } else {
                wish.setReservoir(user);
            }
            gift.setUser(user);
            user.setGifts(List.of(gift));
            wish.setWishStatus(Status.RESERVED);
        } else
            return new SimpleResponse("wish is reserve", "error");

        return new SimpleResponse("ok", "reserved");
    }


    @Transactional
    public SimpleResponse waitStatus(Long wishId) {
        User user = getPrinciple();
        Gift gift = giftRepository
                .findById(wishId).orElseThrow(
                        () -> new NotFoundException("gift not found!"));
        if (gift.getWish().getWishStatus().equals(Status.RESERVED)) {
            user.getGifts().remove(gift);
            giftRepository.delete(gift);
            gift.setUser(null);
            gift.getWish().setWishStatus(Status.WAIT);
        } else
            return new SimpleResponse("wish is wait", "error");

        return new SimpleResponse("ok", "wait");
    }

    public List<BookResponse> getAllReservedWishes() {
        User user = getPrinciple();
        return wishRepository.getALlReservoirWishes(user.getEmail());
    }

    public List<GiftResponse> getAllGifts() {
        User user = getPrinciple();
        return giftRepository.getAllGifts(user.getEmail());
    }

    @Transactional
    public SimpleResponse saveWish(Long wishId) {
        User user = getPrinciple();
        Wish wishUser = wishRepository.findById(wishId).get();
        Wish newWish = new Wish();
        newWish.setWishName(wishUser.getWishName());
        newWish.setLinkToGift(wishUser.getLinkToGift());
        newWish.setDateOfHoliday(wishUser.getDateOfHoliday());
        newWish.setImage(wishUser.getImage());
        newWish.setDescription(wishUser.getDescription());
        Holiday holiday = new Holiday();
        holiday.setName(wishUser.getHoliday().getName());
        holiday.setDateOfHoliday(wishUser.getHoliday().getDateOfHoliday());
        holiday.setImage(wishUser.getHoliday().getImage());
        holiday.setUser(user);
        holidayRepository.save(holiday);
        newWish.setUser(user);
        user.setWishes(List.of(newWish));
        user.setHolidays(List.of(holiday));
        wishRepository.save(wishUser);
        return new SimpleResponse("ok", "ok");
    }
}
