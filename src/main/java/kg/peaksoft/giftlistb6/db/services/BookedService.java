package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.*;
import kg.peaksoft.giftlistb6.db.repositories.*;
import kg.peaksoft.giftlistb6.dto.responses.GiftResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.dto.responses.BookResponse;
import kg.peaksoft.giftlistb6.enums.NotificationType;
import kg.peaksoft.giftlistb6.enums.Status;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookedService {

    private final GiftRepository giftRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final HolidayRepository holidayRepository;

    public User getPrinciple() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> {
                    log.error("User with email:{} not found", email);
                    throw new NotFoundException(String.format("Пользователь с таким электронным адресом: %s не найден!", email));
                });
    }

    @Transactional
    public SimpleResponse reserveWish(Long wishId, boolean is) {
        User user = getPrinciple();
        Wish wish = wishRepository.findById(wishId).orElseThrow(
                () -> {
                    log.error("Wish with id:{} not found", wishId);
                    throw new NotFoundException("Желание не найдено!");
                });
        if (wish.getWishStatus().equals(Status.WAIT)) {
            if (!wish.getUser().equals(user)) {
                Gift gift = new Gift();
                gift.setWish(wish);
                if (is) {
                    wish.setReservoir(null);
                    Notification notification = new Notification();
                    notification.setNotificationType(NotificationType.BOOKED_WISH_ANONYMOUSLY);
                    notification.setWish(gift.getWish());
                    notification.setGift(gift);
                    notification.setCreatedDate(LocalDate.now());
                    notification.setFromUser(user);
                    notification.setUser(gift.getWish().getUser());
                    notification.setIsSeen(false);
                    notificationRepository.save(notification);
                    gift.setUser(user);
                    user.setGifts(List.of(gift));
                    wish.setWishStatus(Status.RESERVED);
                    log.info("Wish with id:{} is reserved anonymously", wishId);
                } else {
                    wish.setReservoir(user);
                }
                if (wish.getWishStatus().equals(Status.WAIT)) {
                    Notification notification = new Notification();
                    notification.setNotificationType(NotificationType.BOOKED_WISH);
                    notification.setWish(gift.getWish());
                    notification.setGift(gift);
                    notification.setCreatedDate(LocalDate.now());
                    notification.setFromUser(user);
                    notification.setUser(gift.getWish().getUser());
                    notification.setIsSeen(false);
                    notificationRepository.save(notification);
                    gift.setUser(user);
                    user.setGifts(List.of(gift));
                    wish.setWishStatus(Status.RESERVED);
                    log.info("Wish with id:{} successfully reserved", wishId);
                }
            } else
                return new SimpleResponse("Вы не можете забронировать свое желание!", "");
            log.error("You can't reserve your gift!");
        } else
            return new SimpleResponse("Желание забронировано!", "");
        log.error("Wish with id: {} is reserved",wishId);


        return new SimpleResponse("Забронировано", "ок");
    }

    @Transactional
    public SimpleResponse waitStatus(Long giftId) {
        User user = getPrinciple();
        Gift gift = giftRepository
                .findById(giftId).orElseThrow(
                        () -> {
                            log.error("Gift with id:{} not found", giftId);
                            throw new NotFoundException("Подарок не найден!");
                        });
        List<Notification> notifications = notificationRepository.findAll();
        for (Notification n : notifications) {
            if (n.getGift() != null) {
                if (n.getFromUser().equals(user) && n.getWish().equals(gift.getWish())) {
                    notificationRepository.deleteById(n.getId());
                    break;
                }
            }
        }
        if (gift.getWish().getWishStatus().equals(Status.RESERVED)) {
            user.getGifts().remove(gift);
            giftRepository.delete(gift);
            gift.setUser(null);
            gift.getWish().setWishStatus(Status.WAIT);
            log.info("Wish with id:{} the reservation was canceled", gift.getWish().getId());
        } else
            return new SimpleResponse("Желание в ожидании", "");

        return new SimpleResponse("Оk", "В ожидании");
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
        Wish wishUser = wishRepository.findById(wishId).orElseThrow(
                ()->new NotFoundException("not found")
        );
        Wish newWish = new Wish();
        newWish.setWishName(wishUser.getWishName());
        newWish.setWishStatus(Status.WAIT);
        newWish.setIsBlock(false);
        newWish.setLinkToGift(wishUser.getLinkToGift());
        newWish.setDateOfHoliday(wishUser.getDateOfHoliday());
        newWish.setImage(wishUser.getImage());
        newWish.setDescription(wishUser.getDescription());
        newWish.setWishStatus(newWish.getWishStatus());
        Holiday holiday1 = new Holiday();
        holiday1.setId(holiday1.getId());
        holiday1.setName(wishUser.getHoliday().getName());
        holiday1.setDateOfHoliday(wishUser.getHoliday().getDateOfHoliday());
        holiday1.setImage(wishUser.getHoliday().getImage());
        holiday1.setUser(user);
        holidayRepository.save(holiday1);
        newWish.setHoliday(holiday1);
        newWish.setUser(user);
        user.addWish(newWish);
        user.addHoliday(holiday1);
        wishRepository.save(wishUser);
        log.info("Wish with id: {} successfully added to {} gifts",wishId,user.getFirstName());
        return new SimpleResponse("Оk", "Оk");
    }
}