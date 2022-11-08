package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.MailingList;
import kg.peaksoft.giftlistb6.dto.responses.MailingListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MailingListRepository extends JpaRepository<MailingList, Long> {

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.MailingListResponse(" +
            "m.id," +
            "m.photo," +
            "m.name," +
            "m.text," +
            "m.createDate)from MailingList m where m.id = :id")
    Optional<MailingListResponse> findMailingById(Long id);
}