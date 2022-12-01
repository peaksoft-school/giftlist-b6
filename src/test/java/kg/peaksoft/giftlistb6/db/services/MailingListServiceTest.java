package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.MailingList;
import kg.peaksoft.giftlistb6.db.repositories.MailingListRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.yml")
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
}