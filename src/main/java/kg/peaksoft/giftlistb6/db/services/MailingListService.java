package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.MailingList;
import kg.peaksoft.giftlistb6.db.repositories.MailingListRepository;
import kg.peaksoft.giftlistb6.dto.requests.MailingListRequest;
import kg.peaksoft.giftlistb6.dto.responses.AllMailingListResponse;
import kg.peaksoft.giftlistb6.dto.responses.MailingListResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MailingListService {

    private final MailingListRepository mailingListRepository;

    public String capitalize(String str) {
        if (str == null || str.length() == 0) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public AllMailingListResponse saveMailingList(MailingListRequest request) {
        MailingList mailingList = new MailingList(request);
        mailingList.setName(capitalize(mailingList.getName()));
        mailingListRepository.save(mailingList);
        log.info("Mailing list saved in database");
        return new AllMailingListResponse(mailingList.getId(),
                mailingList.getImage(),
                mailingList.getName(),
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

    public List<AllMailingListResponse> getAllMailingLists() {
        return mailingListRepository.findAllMailingList();
    }

    public SimpleResponse delete(Long id){
        MailingList mailingList = mailingListRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Не найден!")
        );
        mailingListRepository.delete(mailingList);
        return new SimpleResponse("Удалено","Ок");
    }
}