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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
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
                () -> {
                    log.error("Charity with id: {} not found", id);
                    throw new NotFoundException("Благотворительность с id: " + id + " не найдена!");
                }
        );

        InnerCharityResponse response = new InnerCharityResponse(charity.getId(), charity.getImage(), charity.getName(), charity.getDescription(),
                charity.getCategory().getName(), charity.getSubCategory().getName(), charity.getCondition(), charity.getCreatedAt(),
                charity.getCharityStatus());

        UserCharityResponse userCharityResponse = new UserCharityResponse(
                charity.getUser().getId(),
                charity.getUser().getFirstName(), charity.getUser().getLastName(),
                charity.getUser().getImage());
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
        charity.setIsBlock(false);
        charityRepository.save(charity);
        log.info("Charity successfully saved in database");
        return new YourCharityResponse(charity.getId(), charity.getImage());
    }

    public CharityResponses getAll() {
        User user = getPrinciple();
        CharityResponses charityResponses = new CharityResponses();
        charityResponses.setOtherCharityResponses(charityRepository.getAll(user.getEmail()));
        charityResponses.setYourCharityResponses(charityRepository.getAllMyCharity(user.getEmail()));
        return charityResponses;
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
        log.info("Charity with id: {} successfully updated", id);
        return new InnerPageCharityResponse(charity1.getId(), charity1.getImage(), charity1.getName(),
                charity1.getDescription(), charity1.getCategory().getName(), charity1.getSubCategory().getName(),
                charity1.getCondition(), charity1.getCreatedAt(), charity1.getCharityStatus(), charity1.getUser().getId(),
                charity1.getUser().getImage(), charity1.getUser().getFirstName(), charity1.getUser().getLastName());
    }

    public SimpleResponse deleteCharityByAdmin(Long id) {
        Charity charity = charityRepository.findById(id).orElseThrow(() -> new NotFoundException("not found"));
        charity.setUser(null);
        charity.setCategory(null);
        charity.setSubCategory(null);
        charity.setReservoir(null);
        charityRepository.deleteById(id);
        return new SimpleResponse("deleted", "OK");
    }

    public SimpleResponse deleteCharityById(Long id) {
        User user = getPrinciple();
        if (!user.getCharities().contains(charityRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найден!")))) {
            log.error("You can't delete other charities! ");
            throw new NotFoundException("Вы не можете удалять другие благотворительности");
        }
        charityRepository.deleteCharityById(id, user.getId());
        log.info("Charity with id: {} successfully deleted ", id);
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
                    charity.setReservoir(user);
                    charity.setUser(charity.getUser());
                    user.setCharities(List.of(charity));
                    charity.setCharityStatus(Status.RESERVED_ANONYMOUSLY);
                    log.info("Charity with id: {} reserved anonymously ", charityId);
                    return new SimpleResponse("Забронирован анонимно", "ок");
                } else {
                    charity.setReservoir(user);
                }
                charity.setUser(charity.getUser());
                user.setCharities(List.of(charity));
                charity.setCharityStatus(Status.RESERVED);
                log.info("Charity with id: {} successfully reserved ", charityId);
            } else {
                log.warn("You can't reserved your charity!");
                return new SimpleResponse("Вы не можете зарезервировать свою благотворительность!", "ERROR");
            }
        } else {
            log.warn("Charity with id: {} is reserved", charityId);
            return new SimpleResponse("Благотворительность в резерве", "RESERVED");
        }

        return new SimpleResponse("Оk", "Бронирован!");
    }

    @Transactional
    public SimpleResponse unReserve(Long id) {
        User user = getPrinciple();
        Charity charity = charityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Не найден!")
        );
        if (charity.getCharityStatus().equals(Status.RESERVED) || charity.getCharityStatus().equals(Status.RESERVED_ANONYMOUSLY)) {
            if (charity.getReservoir().equals(user)) {
                charity.setReservoir(null);
                charity.setCharityStatus(Status.WAIT);
            } else {
                return new SimpleResponse("Не ваш благотворительность", "");
            }
        } else {
            return new SimpleResponse("Благотворительность в ожидании", "WAIT");
        }
        return new SimpleResponse("ok", "ok");
    }

    public SearchAllResponse searchCharity(String text, String condition, String category, String subCategory) {
        if (Objects.equals(condition, "Все")) {
            condition = null;
        } else if (Objects.equals(condition, "все")) {
            condition = null;
        }
        User userPr = getPrinciple();
        SearchAllResponse searchResponse = new SearchAllResponse();
        List<YourCharityResponse> myCharities = charityRepository.getAllMyCharity(userPr.getEmail());
        List<Charity> charities = charityRepository.searchCharity(text, condition, category, subCategory, userPr.getEmail());
        searchResponse.setMyCharities(myCharities);
        searchResponse.setSearchOthers(viewAll(charities));
        return searchResponse;
    }

    public SearchCharityResponse viewMapper(Charity charity) {
        SearchCharityResponse searchCharityResponse = new SearchCharityResponse();
        searchCharityResponse.setCharityId(charity.getId());
        searchCharityResponse.setSaveUserResponse(new SearchUserResponse(charity.getUser().getId(), charity.getUser().getImage(),
                charity.getUser().getFirstName() + " " + charity.getUser().getLastName()));
        searchCharityResponse.setCharityImage(charity.getImage());
        searchCharityResponse.setCharityName(charity.getName());
        searchCharityResponse.setCharityCondition(charity.getCondition());
        searchCharityResponse.setCreatedAt(charity.getCreatedAt());
        searchCharityResponse.setStatus(charity.getCharityStatus());
        if (charity.getReservoir() == null) {
            searchCharityResponse.setReservoirUser(new UserFeedResponse());
        } else {
            searchCharityResponse.setReservoirUser(new UserFeedResponse(charity.getReservoir().getId(), charity.getReservoir().getImage()));
        }
        return searchCharityResponse;
    }

    public List<SearchCharityResponse> viewAll(List<Charity> charities) {
        List<SearchCharityResponse> responseList = new ArrayList<>();
        for (Charity charity : charities) {
            responseList.add(viewMapper(charity));
        }
        return responseList;
    }

    @Transactional
    public InnerCharityResponse getCharityByIdWithAdmin(Long id) {
        Charity charity = charityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Не найден!")
        );
        InnerCharityResponse response = new InnerCharityResponse(charity.getId(), charity.getImage(), charity.getName(),
                charity.getDescription(), charity.getCategory().getName(), charity.getSubCategory().getName(),
                charity.getCondition(), charity.getCreatedAt(),charity.getCharityStatus(), charity.getIsBlock());
        UserCharityResponse userCharityResponse = new UserCharityResponse(charity.getUser().getId(), charity.getUser().getFirstName(),
                charity.getUser().getLastName(), charity.getUser().getImage(),charity.getUser().getUserInfo().getPhoneNumber());
        response.setUserCharityResponse(userCharityResponse);
        return response;
    }

    @Transactional
    public CharityResponses getAllCharityResponseByAdmin() {
        CharityResponses charityResponse = new CharityResponses();
        List<OtherCharityResponse> otherCharityResponses = charityRepository.getAllForAdmin();
        charityResponse.setOtherCharityResponses(otherCharityResponses);
        return charityResponse;
    }

    @Transactional
    public SimpleResponse blockCharity(Long id) {
        Charity charity = charityRepository.findById(id).orElseThrow(() -> {
            log.error("Charity with id:{} not found", id);
            throw new NotFoundException("Благотворительность с таким id: %s не найден");
        });
        charity.setIsBlock(true);
        log.info("Charity with id:{} is block", id);
        return new SimpleResponse("Заблокирован", "Благотворительность заблокирован");
    }

    @Transactional
    public SimpleResponse unblockCharity(Long id) {
        Charity charity = charityRepository.findById(id).orElseThrow(() -> {
            log.error("Charity with id:{} not found", id);
            throw new NotFoundException("Благотворительность с таким id: %s не найден");
        });
        charity.setIsBlock(false);
        log.info("Charity with id:{} is unblock", id);
        return new SimpleResponse("Разблокирован", "Благотворительность разблокирован");
    }
}