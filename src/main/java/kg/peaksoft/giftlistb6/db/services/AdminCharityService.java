package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Charity;
import kg.peaksoft.giftlistb6.db.repositories.CharityRepository;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminCharityService {

    private final CharityRepository charityRepository;

    public InnerCharityResponse getCharityById(Long id) {
        Charity charity = charityRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Не найден!")
        );

        InnerCharityResponse response = new InnerCharityResponse(charity.getId(),charity.getImage(),charity.getName(),
                charity.getDescription(),charity.getCategory().getName(),charity.getSubCategory().getName(),
                charity.getCondition(),charity.getCreatedAt(),charity.getCharityStatus());

        ReservoirResponse reservoirResponse = new ReservoirResponse(charity);

        UserCharityResponse userCharityResponse = new UserCharityResponse(charity.getUser().getId(),charity.getUser().getFirstName(),
                charity.getUser().getLastName(), charity.getUser().getImage());

        response.setUserCharityResponse(userCharityResponse);
        if (charity.getReservoir()==null){
            response.setReservoirResponse(new ReservoirResponse());
        }
        response.setReservoirResponse(reservoirResponse);
        return response;
    }

    @Transactional
        public CharityResponses getAllCharityResponse() {
            CharityResponses charityResponse = new CharityResponses();
            List<OtherCharityResponse> otherCharityResponses = charityRepository.getAllCharities();
            for (Charity c : charityRepository.findAll()) {
                for (OtherCharityResponse o : otherCharityResponses) {
                    if (o.getReservoir() == null) {
                    o.setReservoir(new ReservoirResponse());
                    }
                        ReservoirResponse reservoirResponse1 = new ReservoirResponse(o.getReservoir().getId(), o.getReservoir().getImage());
                        o.setReservoir(reservoirResponse1);
                }
                charityResponse.setOtherCharityResponses(otherCharityResponses);
            }
            return charityResponse;
        }
}