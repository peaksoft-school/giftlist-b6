package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.CharityRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.responses.AdminResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
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
}