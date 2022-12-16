package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.FriendRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.yml")
class FriendServiceTest {

    @Autowired
    private FriendRepository friendRepository;

    @Test
    void getAllFriends() {
        User user = new User("User");
        int size = friendRepository.getAllFriends(user.getEmail()).size();
        assertThat(size).isGreaterThan(0);
    }

    @Test
    void getAllRequests() {
        User user = new User("User");
        int size = friendRepository.getAllRequests(user.getEmail()).size();
        assertThat(size).isGreaterThan(0);
    }
}