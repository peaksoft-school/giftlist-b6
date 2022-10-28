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

import java.time.LocalDate;
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
                        n.getUser().getId(),
                        n.getFromUser().getFirstName() + " " + n.getFromUser().getLastName(),
                        n.getFromUser().getPhoto(), LocalDate.now(),
                        NotificationType.ADD_WISH,
                        "добавил желаемый подарок",
                        n.getWish().getWishName()));
            }
            if (n.getNotificationType().equals(NotificationType.REQUEST_TO_FRIEND)) {
                responses.add(new NotificationResponse(
                        n.getUser().getId(),
                        n.getFromUser().getFirstName() + " " + n.getFromUser().getLastName(),
                        n.getFromUser().getPhoto(), LocalDate.now(),
                        NotificationType.REQUEST_TO_FRIEND,
                        "отправил запрос в друзья",
                        null));
            }
        }
        allNotifications.setResponseList(responses);
        return allNotifications;
    }
}
