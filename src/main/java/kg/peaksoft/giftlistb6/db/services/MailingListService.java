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
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailingListService {

    private final MailingListRepository mailingListRepository;

    public AllMailingListResponse saveMailingList(MailingListRequest request) {
        MailingList mailingList = convertToEntity(request);
        mailingListRepository.save(mailingList);
        log.info("Mailing list saved in database ");
        return convertToResponse(mailingList);
    }

    @Transactional
    public MailingList convertToEntity(MailingListRequest request) {
        MailingList mailingList = new MailingList();
        mailingList.setName(request.getName());
        mailingList.setImage(request.getImage());
        mailingList.setText(request.getText());
        mailingList.setCreatedAt(LocalDateTime.now());
        return mailingList;
    }

    @Transactional
    public AllMailingListResponse convertToResponse(MailingList mailingList) {
        AllMailingListResponse response = new AllMailingListResponse();
        response.setId(mailingList.getId());
        response.setName(mailingList.getName());
        response.setImage(mailingList.getImage());
        response.setCreatedAt(LocalDateTime.now());
        return response;
    }

    public List<AllMailingListResponse> convertToAllView(List<MailingList> mailingLists) {
       List<AllMailingListResponse> list = new ArrayList<>();
       for (MailingList mailingList : mailingLists) {
           list.add(convertToResponse(mailingList));
       }
       return list;
    }

    public MailingListResponse getId(Long id) {
         return mailingListRepository.findMailingById(id).orElseThrow(
                ()-> {
                    log.error("Mailing list with id:{} not found!",id);
                    throw new NotFoundException(String.format("Рассылка с id: %s не найдена!", id));
                }
        );
    }

    public List<AllMailingListResponse> getAllMailingLists() {
        return convertToAllView(mailingListRepository.findAll());
    }
}