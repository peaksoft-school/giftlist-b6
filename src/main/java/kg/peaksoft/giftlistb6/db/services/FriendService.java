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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final NotificationRepository notificationRepository;

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("user with email: %s not found", email)));
    }

    public List<FriendInfoResponse> getAllFriends() {
        User user = getAuthPrincipal();
        return friendRepository.getAllFriends(user.getEmail());
    }

    public List<FriendInfoResponse> getAllRequests() {
        User user = getAuthPrincipal();
        return friendRepository.getAllRequests(user.getEmail());
    }

    @Transactional
    public SimpleResponse sendRequestToFriend(Long friendId) {
        User user = getAuthPrincipal();
        User friend = userRepository.findById(friendId).orElseThrow(
                () -> new NotFoundException(String.format("user with id: %s not found", friendId)));
        if (user.equals(friend)) {
            throw new BadRequestException("вы не можете отправить запрос себе!");
        }
        if (friend.getRequests().contains(user)) {
            throw new BadRequestException("вы уже отправили запрос!");
        }
        if (friend.getFriends().contains(user)) {
            throw new BadRequestException("вы уже в друзьях!");
        }
        friend.setRequests(List.of(user));
        Notification notification = new Notification();
        notification.setUser(friend);
        notification.setRequestToFriend(user);
        notification.setIsSeen(false);
        notification.setCreatedDate(LocalDate.now());
        notification.setNotificationType(NotificationType.REQUEST_TO_FRIEND);
        notificationRepository.save(notification);
        return new SimpleResponse("successful", "REQUEST TO FRIEND");
    }

    @Transactional
    public SimpleResponse acceptRequest(Long senderUserId) {
        User user = getAuthPrincipal();
        User request = userRepository.findById(senderUserId).orElseThrow(
                () -> new NotFoundException(String.format("User with id: %s not found!", senderUserId)));
        for (Notification n : user.getNotifications()) {
            if (n.getRequestToFriend().getId().equals(senderUserId)) {
                notificationRepository.delete(n);
                user.getNotifications().remove(n);
                break;
            }
        }
        if (user.getRequests().contains(request)) {
            user.setFriends(List.of(request));
            user.getRequests().remove(request);
        } else {
            return new SimpleResponse("request not found", "");
        }
        return new SimpleResponse("successful", "FRIEND");
    }

    @Transactional
    public SimpleResponse rejectRequest(Long senderUserId) {
        User user = getAuthPrincipal();
        User sender = userRepository.findById(senderUserId).orElseThrow(
                () -> new NotFoundException(String.format("user with id: %s not found", senderUserId)));
        for (Notification n : user.getNotifications()) {
            if (n.getRequestToFriend().getId().equals(senderUserId)) {
                notificationRepository.delete(n);
                user.getNotifications().remove(n);
            }
            break;
        }
        if (user.getRequests().contains(sender)) {
            user.getRequests().remove(sender);
        } else {
            return new SimpleResponse("request not found", "");
        }
        return new SimpleResponse("successful", "NOT FRIEND");
    }

    @Transactional
    public SimpleResponse deleteFromFriends(Long friendId) {
        User user = getAuthPrincipal();
        User friend = userRepository.findById(friendId).orElseThrow(
                () -> new NotFoundException(String.format("user with id: %s not found", friendId)));
        if (user.getFriends().contains(friend)) {
            user.getFriends().remove(friend);
            friend.getFriends().remove(user);
        } else {
            return new SimpleResponse("friend not found", "");
        }
        return new SimpleResponse("successful", "NOT FRIEND");
    }

    @Transactional
    public SimpleResponse cancelRequestToFriend(Long friendId) {
        User user = getAuthPrincipal();
        User friend = userRepository.findById(friendId).orElseThrow(
                () -> new NotFoundException(String.format("user with id %s not found", friendId)));
        for (Notification n : friend.getNotifications()) {
            if (n.getRequestToFriend().getId().equals(user.getId())) {
                notificationRepository.delete(n);
                friend.getNotifications().remove(n);
            }
            break;
        }
        if (friend.getRequests().contains(user)) {
            friend.getRequests().remove(user);
        } else {
            return new SimpleResponse("request not found", "");
        }
        return new SimpleResponse("successful", "NOT FRIEND");
    }

}
