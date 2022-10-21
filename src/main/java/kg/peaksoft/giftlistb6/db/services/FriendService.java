package kg.peaksoft.giftlistb6.db.service;

import kg.peaksoft.giftlistb6.db.model.Notification;
import kg.peaksoft.giftlistb6.db.model.User;
import kg.peaksoft.giftlistb6.db.repository.FriendRepository;
import kg.peaksoft.giftlistb6.db.repository.NotificationRepository;
import kg.peaksoft.giftlistb6.db.repository.UserRepository;
import kg.peaksoft.giftlistb6.dto.responses.FriendInfoResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.enums.NotificationType;
import kg.peaksoft.giftlistb6.exception.BadRequestException;
import kg.peaksoft.giftlistb6.exception.NotFoundException;
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
        } else if (user.getFriends().contains(friend)) {
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
        for (Notification n: user.getNotifications()) {
            System.out.println("for");
//            for (User u:user.getFriends()) {
                if (n.getRequestToFriend().equals(request)){
                    System.out.println("ifff");
                    notificationRepository.deleteById(n.getId());
                    System.out.println("delete");
                }
//            }
        if (user.getRequests().contains(request)) {
            user.setFriends(List.of(request));
            user.getRequests().remove(request);
        } else
            return new SimpleResponse("request not found", "");

        }
        return new SimpleResponse("successful", "FRIEND");
    }

    @Transactional
    public SimpleResponse rejectRequest(Long senderUserId) {
        User user = getAuthPrincipal();
        User sender = userRepository.findById(senderUserId).orElseThrow(
                () -> new NotFoundException(String.format("user with id: %s not found", senderUserId)));
        if (user.getRequests().contains(sender)) {
            user.getRequests().remove(sender);
        } else
            return new SimpleResponse("request not found", "");

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
        } else
            return new SimpleResponse("friend not found", "");

        return new SimpleResponse("successful", "NOT FRIEND");
    }

    @Transactional
    public SimpleResponse cancelRequestToFriend(Long friendId) {
        User user = getAuthPrincipal();
        User friend = userRepository.findById(friendId).orElseThrow(
                () -> new NotFoundException(String.format("user with id %s not found", friendId)));
        if (user.getRequests().contains(friend)) {
            user.getRequests().remove(friend);
        } else
            return new SimpleResponse("request not found", "");

        return new SimpleResponse("successful", "NOT FRIEND");
    }

}
