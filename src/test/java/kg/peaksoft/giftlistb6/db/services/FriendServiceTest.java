package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.FriendRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FriendServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;

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
}