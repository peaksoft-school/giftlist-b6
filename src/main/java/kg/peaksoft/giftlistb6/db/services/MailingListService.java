package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.MailingList;
import kg.peaksoft.giftlistb6.db.repositories.MailingListRepository;
import kg.peaksoft.giftlistb6.dto.requests.MailingListRequest;
import kg.peaksoft.giftlistb6.dto.responses.AllMailingListResponse;
import kg.peaksoft.giftlistb6.dto.responses.MailingListResponse;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailingListService {

    private final MailingListRepository mailingListRepository;

    public AllMailingListResponse saveMailingList(MailingListRequest request) {
        MailingList mailingList = new MailingList(request);
        mailingListRepository.save(mailingList);
        log.info("Mailing list saved in database");
        return new AllMailingListResponse(mailingList.getId(),
                mailingList.getName(),
                mailingList.getImage(),
                mailingList.getCreatedAt());
    }

    public MailingListResponse getId(Long id) {
        return mailingListRepository.findMailingById(id).orElseThrow(
                () -> {
                    log.error("Mailing list with id:{} not found!", id);
                    throw new NotFoundException(String.format("Рассылка с id: %s не найдена!", id));
                }
        );
    }

    public Deque<AllMailingListResponse> getAllMailingLists() {
        return mailingListRepository.findAllMailingList();
    }
}