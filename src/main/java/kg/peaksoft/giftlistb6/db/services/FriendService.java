package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Notification;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.FriendRepository;
import kg.peaksoft.giftlistb6.db.repositories.NotificationRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.responses.FriendInfoResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.enums.NotificationType;
import kg.peaksoft.giftlistb6.exceptions.BadRequestException;
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
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final NotificationRepository notificationRepository;

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> {
                    log.error("User with email: {} not found!",email);
                    throw new NotFoundException(String.format("Пользователь с таким электронным адресом: %s не найден", email));
                });
    }

    public List<FriendInfoResponse> getAllFriends() {
        User user = getAuthPrincipal();
        log.info("User with email: {} seen all friends",user.getEmail());
        return friendRepository.getAllFriends(user.getEmail());
    }

    public List<FriendInfoResponse> getAllRequests() {
        User user = getAuthPrincipal();
        log.info("User with email : {} seen all requests ",user.getEmail());
        return friendRepository.getAllRequests(user.getEmail());
    }

    @Transactional
    public SimpleResponse sendRequestToFriend(Long friendId) {
        User user = getAuthPrincipal();
        User friend = userRepository.findById(friendId).orElseThrow(
                () ->{
                    log.error("User with id:{} not found",friendId);
                     throw new NotFoundException(String.format("Пользователь с таким id: %s не найден!", friendId));
                });
        if (user.equals(friend)) {
            log.error("User can't send a request to yourself");
            throw new BadRequestException("Вы не можете отправить запрос себе!");
        }
        if (friend.getRequests().contains(user)) {
            log.error("Request already send");
            throw new BadRequestException("Вы уже отправили запрос!");
        }
        if (friend.getFriends().contains(user) || user.getFriends().contains(friend)) {
            log.error("Already friends");
            throw new BadRequestException("Вы уже в друзьях!");
        }
        friend.addRequest(user);
        Notification notification = new Notification();
        notification.setUser(friend);
        notification.setFromUser(user);
        notification.setIsSeen(false);
        notification.setCreatedDate(LocalDate.now());
        notification.setNotificationType(NotificationType.REQUEST_TO_FRIEND);
        notificationRepository.save(notification);
        log.info("User with email: {} successfully send a request to friend :{}",user.getEmail(),friend.getEmail());
        return new SimpleResponse("Удачно", "Запрос в друзья");
    }

    @Transactional
    public SimpleResponse acceptRequest(Long senderUserId) {
        User user = getAuthPrincipal();
        User request = userRepository.findById(senderUserId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким id: %s не найден!", senderUserId)));
        for (Notification n : user.getNotifications()) {
            if (n.getFromUser().getId().equals(senderUserId)) {
                notificationRepository.delete(n);
                user.getNotifications().remove(n);
                break;
            }
        }
        if (user.getRequests().contains(request)) {
            user.addFriend(request);
            request.addFriend(user);
            user.getRequests().remove(request);
        } else {
            return new SimpleResponse("Запрос не найден", "");
        }
        log.info("Successfully accepted request");
        return new SimpleResponse("Удачно", "В друзьях");
    }

    @Transactional
    public SimpleResponse rejectRequest(Long senderUserId) {
        User user = getAuthPrincipal();
        User sender = userRepository.findById(senderUserId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким id: %s не найден! ", senderUserId)));
        for (Notification n : user.getNotifications()) {
            if (n.getFromUser().getId().equals(senderUserId)) {
                notificationRepository.delete(n);
                user.getNotifications().remove(n);
            }
            break;
        }
        if (user.getRequests().contains(sender)) {
            user.getRequests().remove(sender);
        } else {
            return new SimpleResponse("Запрос не найден", "");
        }
        log.info("User with email: {} rejected request ",user.getEmail());
        return new SimpleResponse("Удачно", "Не в друзьях");
    }

    @Transactional
    public SimpleResponse deleteFromFriends(Long friendId) {
        User user = getAuthPrincipal();
        User friend = userRepository.findById(friendId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким id: %s не найден!", friendId)));
        if (user.getFriends().contains(friend)) {
            user.getFriends().remove(friend);
            friend.getFriends().remove(user);

        } else {
            return new SimpleResponse("Не найден", "");
        }
        log.info("Deleted from friends");
        return new SimpleResponse("Удачно", "Не в друзьях");
    }

    @Transactional
    public SimpleResponse cancelRequestToFriend(Long friendId) {
        User user = getAuthPrincipal();
        User friend = userRepository.findById(friendId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким id: %s не найден!", friendId)));
        for (Notification n : friend.getNotifications()) {
            if (n.getFromUser().getId().equals(user.getId())) {
                notificationRepository.delete(n);
                friend.getNotifications().remove(n);
            }
            break;
        }
        if (friend.getRequests().contains(user)) {
            friend.getRequests().remove(user);
        } else {
            return new SimpleResponse("Не найден", "");
        }
        log.info("Cancel request to friend");
        return new SimpleResponse("Удачно", "Отменено");
    }

}
