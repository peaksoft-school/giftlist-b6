package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.FriendRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FriendServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private FriendService friendService;

    @Test
    void getAllFriends() {
        User user = userRepository.findById(5L).get();
        int size = friendRepository.getAllFriends(user.getEmail()).size();
        assertThat(size).isGreaterThan(0);
    }

    @Test
    void getAllRequests() {
        User user = userRepository.findById(3L).get();
        int size = friendRepository.getAllRequests(user.getEmail()).size();
        assertThat(size).isGreaterThan(0);
    }

    @Test
    void sendRequestToFriend() {
        User user = userRepository.findById(9L).get();
        User friend = userRepository.findById(6L).get();
        int before = friendRepository.getAllRequests(user.getEmail()).size();
//        friendService.sendRequestToFriend(5L);
        user.setRequests(List.of(friend));
        int size = friendRepository.getAllRequests(user.getEmail()).size();
        assertThat(size).isGreaterThan(before);

    }

    @Test
    void acceptRequest() {
    }

    @Test
    void deleteFromFriends() {
    }

    @Test
    void cancelRequestToFriend() {
    }
}