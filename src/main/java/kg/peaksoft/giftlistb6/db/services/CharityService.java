package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Charity;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.CategoryRepository;
import kg.peaksoft.giftlistb6.db.repositories.CharityRepository;
import kg.peaksoft.giftlistb6.db.repositories.SubCategoryRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.requests.CharityRequest;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.enums.Status;
import kg.peaksoft.giftlistb6.exceptions.BadRequestException;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CharityService {

    private final CharityRepository charityRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public User getPrinciple() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("Не найден!"));
    }

    public InnerCharityResponse getCharityById(Long id) {
        Charity charity = charityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Благотворительность с id: " + id + " не найдена!")
        );

        InnerCharityResponse response = new InnerCharityResponse(charity.getId(), charity.getImage(), charity.getName(), charity.getDescription(),
                charity.getCategory().getName(), charity.getSubCategory().getName(), charity.getCondition(), charity.getCreatedDate(),
                charity.getCharityStatus());

        UserCharityResponse userCharityResponse = new UserCharityResponse(
                charity.getUser().getId(),
                charity.getUser().getFirstName(),charity.getUser().getLastName(),
                charity.getUser().getPhoto());
        if (charity.getReservoir() == null) {
            charity.setReservoir(null);
        }
        ReservoirResponse reservoirResponse = new ReservoirResponse(charity);
        response.setUserCharityResponse(userCharityResponse);
        if (charity.getReservoir() == null) {
            response.setReservoirResponse(new ReservoirResponse());
        }
        response.setReservoirResponse(reservoirResponse);
        return response;
    }

    public YourCharityResponse saveCharity(CharityRequest charityRequest) {
        if (!categoryRepository.findByName(charityRequest.getCategory()).getSubCategory()
                .contains(subCategoryRepository.findByName(charityRequest.getSubCategory()))) {
            throw new BadRequestException();
        }
        User user = getPrinciple();
        Charity charity = new Charity(charityRequest);
        charity.setCategory(categoryRepository.findByName(charityRequest.getCategory()));
        charity.setSubCategory(subCategoryRepository.findByName(charityRequest.getSubCategory()));
        user.setCharities(List.of(charity));
        charity.setUser(user);
        charityRepository.save(charity);
        return new YourCharityResponse(charity.getId(), charity.getImage());
    }

    @Transactional
    public CharityResponses getAllCharityResponse() {
        User user = getPrinciple();
        CharityResponses charityResponse = new CharityResponses();
        List<YourCharityResponse> yourCharityResponses = charityRepository.getAllMyCharity(user.getEmail());
        List<OtherCharityResponse> otherCharityResponses = charityRepository.getAllOther(user.getEmail());
        for (Charity c : charityRepository.findAll()) {
            for (OtherCharityResponse o : otherCharityResponses) {
                if (o.getReservoir() != null) {
                    ReservoirResponse response = new ReservoirResponse(o.getReservoir().getId(), o.getReservoir().getImage());
                    o.setReservoir(response);
                }
                o.setReservoir(new ReservoirResponse());
            }
            charityResponse.setYourCharityResponses(yourCharityResponses);
            charityResponse.setOtherCharityResponses(otherCharityResponses);
        }
            return charityResponse;
    }

    @Transactional
    public InnerPageCharityResponse updateCharity(Long id, CharityRequest charityRequest) {
        User user = getPrinciple();
        if (charityRequest == null) {
            return null;
        }
        Charity charity1 = charityRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найден!"));
        if (charity1.getUser().equals(user)) {
            charity1.setImage(charityRequest.getImage());
            charity1.setUser(user);
            charity1.setName(charityRequest.getName());
            charity1.setCondition(charityRequest.getCondition());
            charity1.setCategory(categoryRepository.findByName(charityRequest.getCategory()));
            charity1.setSubCategory(subCategoryRepository.findByName(charityRequest.getSubCategory()));
            charity1.setDescription(charityRequest.getDescription());
            charityRepository.save(charity1);
        }
        return new InnerPageCharityResponse(charity1.getId(), charity1.getImage(), charity1.getName(),
                charity1.getDescription(), charity1.getCategory().getName(), charity1.getSubCategory().getName(),
                charity1.getCondition(), charity1.getCreatedDate(), charity1.getCharityStatus(), charity1.getUser().getId(),
                charity1.getUser().getPhoto(), charity1.getUser().getFirstName() , charity1.getUser().getLastName());
    }

    public SimpleResponse deleteCharityById(Long id) {
        User user = getPrinciple();
        if (!user.getCharities().contains(charityRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найден!")))) {
            throw new NotFoundException("Вы не можете удалять другие благотворительности");
        }
        charityRepository.deleteCharityById(id, user.getId());
        return new SimpleResponse("Благотворительность успешно удалено!", "");
    }

    @Transactional
    public SimpleResponse reserveCharity(Long charityId, boolean is) {
        User user = getPrinciple();
        Charity charity = charityRepository.findById(charityId).orElseThrow(
                () -> new NotFoundException("Не найден!"));
        if (charity.getCharityStatus().equals(Status.WAIT)) {
            if (!charity.getUser().equals(user)) {
                charity.setReservoir(user);
                if (is) {
                    charity.setReservoir(null);
                } else {
                    charity.setReservoir(user);
                }
                charity.setUser(charity.getUser());
                user.setCharities(List.of(charity));
                charity.setCharityStatus(Status.RESERVED);
            } else
                return new SimpleResponse("Вы не можете зарезервировать свою благотворительность!", "ERROR");
        } else
            return new SimpleResponse("Благотворительность в резерве", "RESERVED");

        return new SimpleResponse("Оk", "Бронирован!");
    }
}