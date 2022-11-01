package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Notification;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.responses.AllNotificationsResponse;
import kg.peaksoft.giftlistb6.dto.responses.NotificationResponse;
import kg.peaksoft.giftlistb6.enums.NotificationType;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("user with email: %s not found", email)));
    }

    public AllNotificationsResponse getAllNotifications() {
        User user = getAuthPrincipal();
        AllNotificationsResponse allNotifications = new AllNotificationsResponse();
        List<NotificationResponse> responses = new ArrayList<>();
        for (Notification n : user.getNotifications()) {
            if (n.getNotificationType().equals(NotificationType.ADD_WISH)) {
                responses.add(new NotificationResponse(
                        n.getFromUser().getId(),
                        n.getFromUser().getFirstName() + " " + n.getFromUser().getLastName(),
                        n.getFromUser().getPhoto(), n.getCreatedDate(),
                        NotificationType.ADD_WISH,
                        "добавил желаемый подарок"));
            }
            if (n.getNotificationType().equals(NotificationType.REQUEST_TO_FRIEND)) {
                responses.add(new NotificationResponse(
                        n.getFromUser().getId(),
                        n.getFromUser().getFirstName() + " " + n.getFromUser().getLastName(),
                        n.getFromUser().getPhoto(), n.getCreatedDate(),
                        NotificationType.REQUEST_TO_FRIEND,
                        "отправил запрос в друзья"));
            }
            if (n.getNotificationType().equals(NotificationType.BOOKED_WISH)) {
                responses.add(new NotificationResponse(
                        n.getFromUser().getId(),
                        n.getFromUser().getFirstName() + " " + n.getFromUser().getLastName(),
                        n.getFromUser().getPhoto(),
                        n.getCreatedDate(),
                        NotificationType.BOOKED_WISH,
                        n.getWish().getWishName() + " было забронировано " + n.getFromUser().getFirstName() + " " + n.getFromUser().getLastName()));
            }
            if (n.getNotificationType().equals(NotificationType.BOOKED_WISH_ANONYMOUSLY)) {
                responses.add(new NotificationResponse(
                        n.getFromUser().getId(),
                        n.getFromUser().getFirstName() + " " + n.getFromUser().getLastName(),
                        n.getWish().getImage(),
                        n.getCreatedDate(),
                        NotificationType.BOOKED_WISH_ANONYMOUSLY,
                        n.getWish().getWishName() + " было забронировано анонимным пользователем "));
            }
        }
        allNotifications.setResponseList(responses);
        return allNotifications;
    }
}
