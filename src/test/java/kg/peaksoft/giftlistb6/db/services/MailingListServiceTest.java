package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.MailingList;
import kg.peaksoft.giftlistb6.db.repositories.MailingListRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class MailingListServiceTest {

    @Autowired
    private MailingListRepository repository;

    @Test
    @Order(1)
    @Rollback(value = false)
    void saveMailingList() {
        MailingList mailingList = new MailingList(1L,"test","test","test",
                LocalDateTime.now());
        Assertions.assertThat(mailingList.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    void getId() {
        MailingList mailingList = repository.findById(1L).orElse(
                new MailingList(1L,"test","test","test", LocalDateTime.now()));
        Assertions.assertThat(mailingList.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    void getAllMailingLists() {
        List<MailingList> mailingLists = repository.findAll();
        Assertions.assertThat(mailingLists.size()).isGreaterThan(0);
    }
}