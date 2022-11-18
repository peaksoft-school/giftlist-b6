package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Charity;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.CharityRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final CharityRepository charityRepository;

    public List<AdminResponse> getAllUsers() {
        List<User> users = userRepository.getAll();
        List<AdminResponse> userList = new ArrayList<>();
        for (User u : users) {
            userList.add(userService.createUser(u));
        }
        return userList;
    }

    @Transactional
    public SimpleResponse block(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id= %s не найден"));
        user.setIsBlock(true);
        return new SimpleResponse("Заблокирован", "Пользователь заблокирован");
    }

    @Transactional
    public SimpleResponse unBlock(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(
                        "Пользователь с таким id= %s не найден"));
        user.setIsBlock(false);
        return new SimpleResponse("Разблокирован", "Пользователь разблокирован");
    }

    @Transactional
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