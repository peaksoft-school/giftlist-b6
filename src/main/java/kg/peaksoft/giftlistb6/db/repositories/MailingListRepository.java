package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.MailingList;
import kg.peaksoft.giftlistb6.dto.responses.AllMailingListResponse;
import kg.peaksoft.giftlistb6.dto.responses.MailingListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

public interface MailingListRepository extends JpaRepository<MailingList, Long> {

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.MailingListResponse(" +
            "m.id," +
            "m.image," +
            "m.name," +
            "m.text," +
            "m.createdAt)from MailingList m where m.id = :id")
    Optional<MailingListResponse> findMailingById(Long id);

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.AllMailingListResponse(" +
            "m.id," +
            "m.image," +
            "m.name," +
            "m.createdAt) from MailingList m ")
    Deque<AllMailingListResponse> findAllMailingList();
}