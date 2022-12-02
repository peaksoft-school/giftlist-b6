package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.enums.Status;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {

    private final WishRepository wishRepository;

    private final UserRepository userRepository;

    public FeedResponse mapToAllResponse(Wish wish) {
        User user = getAuthPrincipal();
        FeedResponse feedResponse = new FeedResponse();
        if (wish.getWishStatus().equals(Status.RESERVED) && !wish.getReservoir().equals(user)) {
            feedResponse.setIsMy(false);
        } else if (wish.getWishStatus().equals(Status.WAIT)) {
            feedResponse.setIsMy(false);
        } else {
            feedResponse.setIsMy(true);
        }
        feedResponse.setWishId(wish.getId());
        feedResponse.setUserSearchResponse(new SearchUserResponse(wish.getUser().getId(), wish.getUser().getImage(), wish.getUser().getFirstName() + " " + wish.getUser().getLastName()));
        feedResponse.setWishName(wish.getWishName());
        feedResponse.setImage(wish.getImage());
        feedResponse.setStatus(wish.getWishStatus());
        feedResponse.setHoliday(new HolidayResponse(wish.getHoliday().getId(),wish.getHoliday().getName(), wish.getDateOfHoliday()));
        if (wish.getReservoir() == null) {
            feedResponse.setUserFeedResponse(new UserFeedResponse());
        } else {
            feedResponse.setUserFeedResponse(new UserFeedResponse(wish.getReservoir().getId(), wish.getReservoir().getImage()));
        }
        return feedResponse;
    }


    public Deque<FeedResponse> convertAllToResponse(List<Wish> wishes) {
        List<Wish> block = new ArrayList<>();
        Deque<FeedResponse> responses = new ArrayDeque<>();
        for (Wish wish : wishes) {
            if (wish.getIsBlock().equals(true)) {
                block.add(wish);
            } else {
                responses.addFirst(mapToAllResponse(wish));
            }
        }
        User user = getAuthPrincipal();
        for (User friend : user.getFriends()) {
            if (user.getFriends().contains(friend))
                responses.addLast((FeedResponse) wishRepository.getFriendsWishes(friend));
        }
        return responses;
    }

    public Deque<FeedResponse> getAll() {
        User user = getAuthPrincipal();
        return convertAllToResponse(wishRepository.getAllWishes(user.getEmail()));
    }


    public InnerFeedResponse mapToIdResponse(Wish wish) {
        InnerFeedResponse innerFeedResponse = new InnerFeedResponse();
        innerFeedResponse.setWishId(wish.getId());
        innerFeedResponse.setSearchUserResponse(new SearchUserResponse(wish.getUser().getId(), wish.getUser().getImage(), wish.getUser().getFirstName() + " " + wish.getUser().getLastName()));
        innerFeedResponse.setHolidayResponse(new HolidayResponse(wish.getHoliday().getId(),wish.getHoliday().getName(), wish.getHoliday().getDateOfHoliday()));
        innerFeedResponse.setWishName(wish.getWishName());
        innerFeedResponse.setStatus(wish.getWishStatus());
        innerFeedResponse.setDescription(wish.getDescription());
        return innerFeedResponse;
    }

    public InnerFeedResponse getById(Long id) {
        Wish wish = wishRepository.findWishById(id).orElseThrow(() -> {
            log.error("Wish with id: {} not found!", id);
            throw new NotFoundException("Желание с таким id: " + id + " не найдено!");
        });
        return mapToIdResponse(wish);
    }

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким  электронным адресом: %s не найден!", email)));
    }
}
