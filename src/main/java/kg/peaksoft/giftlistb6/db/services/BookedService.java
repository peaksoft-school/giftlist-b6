package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.*;
import kg.peaksoft.giftlistb6.db.repositories.*;
import kg.peaksoft.giftlistb6.dto.responses.BookingResponse;
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
                    wish.setReservoir(user);
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
                    wish.setWishStatus(Status.RESERVED_ANONYMOUSLY);
                    log.info("Wish with id:{} is reserved anonymously", wishId);
                    return new SimpleResponse("Забронирован анонимно", "ок");
                } else {
                    wish.setReservoir(user);
                }
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

            } else {
                log.error("You can't reserve your gift!");
                return new SimpleResponse("Вы не можете забронировать свое желание!", "");
            }
        } else {
            log.error("Wish with id: {} is reserved", wishId);
            return new SimpleResponse("Желание забронировано!", "");
        }


        return new SimpleResponse("Забронировано", "ок");
    }

    @Transactional
    public SimpleResponse waitStatus(Long wishId) {
        User user = getPrinciple();
        Wish gift = wishRepository
                .findById(wishId).orElseThrow(
                        () -> {
                            log.error("Gift with id:{} not found", wishId);
                            throw new NotFoundException("Подарок не найден!");
                        });
        List<Notification> notifications = notificationRepository.findAll();
        for (Notification n : notifications) {
            if (n.getGift() != null) {
                if (n.getFromUser().equals(user) && n.getWish().equals(gift)) {
                    notificationRepository.deleteById(n.getId());
                    break;
                }
            }
        }
        if (gift.getWishStatus().equals(Status.RESERVED) || gift.getWishStatus().equals(Status.RESERVED_ANONYMOUSLY)) {
            if (gift.getReservoir().equals(user)) {
                for (Gift g : user.getGifts()) {
                    if (g.getWish().equals(gift)) {
                        user.getGifts().remove(g);
                        giftRepository.delete(g);
                        break;
                    }
                }
                gift.setReservoir(null);
                gift.setWishStatus(Status.WAIT);
                log.info("Wish with id:{} the reservation was canceled", gift.getId());
            } else {
                return new SimpleResponse("Подарок не ваш!", "");
            }
        } else
            return new SimpleResponse("Желание в ожидании", "");

        return new SimpleResponse("Оk", "В ожидании");
    }

    public List<BookResponse> getAllReservedWishes() {
        User user = getPrinciple();
        return wishRepository.getALlReservoirWishes(user.getEmail());
    }

    @Transactional
    public BookingResponse getAllGifts() {
        User user = getPrinciple();
        BookingResponse bookingResponse = new BookingResponse();
        List<GiftResponse> getAllGifts = giftRepository.getAllGifts(user.getEmail());
        List<GiftResponse> getAllReservedCharity = giftRepository.getAllReservedCharity(user.getEmail());
        bookingResponse.setGetAllGifts(getAllGifts);
        bookingResponse.setGetReservedCharity(getAllReservedCharity);
        return bookingResponse;
    }

    @Transactional
    public SimpleResponse saveWish(Long wishId, Long holidayId) {
        User user = getPrinciple();
        Wish wishUser = wishRepository.findById(wishId).orElseThrow(
                () -> new NotFoundException("Желание не найдено!")
        );
        if (!wishUser.getUser().equals(user)) {
            Wish newWish = new Wish();
            newWish.setWishName(wishUser.getWishName());
            newWish.setWishStatus(Status.WAIT);
            newWish.setIsBlock(false);
            newWish.setReservoir(null);
            newWish.setLinkToGift(wishUser.getLinkToGift());
            newWish.setImage(wishUser.getImage());
            newWish.setDescription(wishUser.getDescription());
            Holiday holiday1 = holidayRepository.findById(holidayId).orElseThrow(
                    () -> new NotFoundException("Праздник не найден!")
            );
            newWish.setDateOfHoliday(holiday1.getDateOfHoliday());
            newWish.setHoliday(holiday1);
            holiday1.setWishes(List.of(newWish));
            newWish.setUser(user);
            user.addWish(newWish);
            wishRepository.save(newWish);
            log.info("Wish with id: {} successfully added to {} gifts", wishId, user.getFirstName());
        } else {
            return new SimpleResponse("Ваше желание!", "");
        }
        return new SimpleResponse("Оk", "Оk");
    }
}