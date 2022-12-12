package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Notification;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.NotificationRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.responses.AllNotificationsResponse;
import kg.peaksoft.giftlistb6.dto.responses.NotificationResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.enums.NotificationType;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Ползователь с таким электронным адресом: %s не найден!", email)));
    }

    public AllNotificationsResponse getAllNotifications() {
        User user = getAuthPrincipal();
        AllNotificationsResponse allNotifications = new AllNotificationsResponse();
        List<NotificationResponse> responses = new ArrayList<>();
        for (Notification n : user.getNotifications()) {
            if (n.getNotificationType().equals(NotificationType.ADD_WISH)) {
                responses.add(new NotificationResponse(
                        n.getFromUser().getId(),
                        n.getWish().getId(),
                        n.getWish().getWishName(),
                        n.getFromUser().getFirstName(),
                        n.getFromUser().getLastName(),
                        n.getFromUser().getImage(),
                        n.getCreatedDate(),
                        NotificationType.ADD_WISH,
                        " добавил желаемый подарок "));
            }
            if (n.getNotificationType().equals(NotificationType.REQUEST_TO_FRIEND)) {
                responses.add(new NotificationResponse(
                        n.getFromUser().getId(),
                        n.getId(), "",
                        n.getFromUser().getFirstName(),
                        n.getFromUser().getLastName(),
                        n.getFromUser().getImage(),
                        n.getCreatedDate(),
                        NotificationType.REQUEST_TO_FRIEND,
                        " отправил запрос в друзья "));
            }
            if (n.getNotificationType().equals(NotificationType.BOOKED_WISH)) {
                responses.add(new NotificationResponse(
                        n.getFromUser().getId(),
                        n.getWish().getId(),
                        n.getWish().getWishName(),
                        n.getFromUser().getFirstName(),
                        n.getFromUser().getLastName(),
                        n.getFromUser().getImage(),
                        n.getCreatedDate(),
                        NotificationType.BOOKED_WISH,
                        " было забронировано "));
            }
            if (n.getNotificationType().equals(NotificationType.BOOKED_WISH_ANONYMOUSLY)) {
                responses.add(new NotificationResponse(
                        n.getFromUser().getId(),
                        n.getWish().getId(),
                        n.getWish().getWishName(),
                        n.getFromUser().getFirstName(),
                        n.getFromUser().getLastName(),
                        n.getWish().getImage(),
                        n.getCreatedDate(),
                        NotificationType.BOOKED_WISH_ANONYMOUSLY,
                        " было забронировано анонимным пользователем "));
            }
        }
        allNotifications.setResponseList(responses);
        log.info("User viewed all notifications");
        return allNotifications;
    }

    public SimpleResponse markAsRead() {
        User user = getAuthPrincipal();
        List<Notification> notifications = notificationRepository.findAll();
        for (Notification n : notifications) {
            if (n.getUser().equals(user)) {
                notificationRepository.deleteById(n.getId());
            }
        }
        log.info("Mark as read all notifications");
        return new SimpleResponse("Удачно", "ок");
    }

    public AllNotificationsResponse getAllNotificationsForAdmin() {
        AllNotificationsResponse response = new AllNotificationsResponse();
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findAll();
        for (Notification n : notifications) {
            if (n.getNotificationType().equals(NotificationType.CREATE_COMPLAINTS)) {
                notificationResponses.add(new NotificationResponse(
                        n.getFromUser().getId(),
                        n.getWish().getId(),
                        n.getWish().getWishName(),
                        n.getFromUser().getFirstName(),
                        n.getFromUser().getLastName(),
                        n.getFromUser().getImage(),
                        n.getCreatedDate(),
                        NotificationType.CREATE_COMPLAINTS,
                        " пожаловался на "));
            }
        }
        response.setResponseList(notificationResponses);
        log.info("Admin has seen all notifications");
        return response;
    }
}