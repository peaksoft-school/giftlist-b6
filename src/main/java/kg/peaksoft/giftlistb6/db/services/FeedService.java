package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final WishRepository wishRepository;

    private final UserRepository userRepository;

    public FeedResponse mapToAllResponse(Wish wish) {
        FeedResponse feedResponse = new FeedResponse();
        feedResponse.setWishId(wish.getId());
        feedResponse.setUserSearchResponse(new SearchUserResponse(wish.getUser().getId(), wish.getUser().getPhoto(), wish.getUser().getFirstName() + " " + wish.getUser().getLastName()));
        feedResponse.setWishName(wish.getWishName());
        feedResponse.setImage(wish.getImage());
        feedResponse.setStatus(wish.getWishStatus());
        feedResponse.setHoliday(new HolidayResponse(wish.getHoliday().getName(), wish.getDateOfHoliday()));
        if (wish.getReservoir() == null) {
            feedResponse.setUserFeedResponse(new UserFeedResponse(null, null));
        } else {
            feedResponse.setUserFeedResponse(new UserFeedResponse(wish.getReservoir().getId(), wish.getReservoir().getPhoto()));
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
            if (user.getFriends().equals(friend))
                responses.addLast((FeedResponse) wishRepository.getFriendsWishes(friend));
            }
        return responses;
    }

    public Deque<FeedResponse> getAll() {
        return convertAllToResponse(wishRepository.getAllWishes());
    }


    public InnerFeedResponse mapToIdResponse(Wish wish) {
        InnerFeedResponse innerFeedResponse = new InnerFeedResponse();
        innerFeedResponse.setWishId(wish.getId());
        innerFeedResponse.setSearchUserResponse(new SearchUserResponse(wish.getUser().getId(), wish.getUser().getPhoto(), wish.getUser().getFirstName() + " " + wish.getUser().getLastName()));
        innerFeedResponse.setHolidayResponse(new HolidayResponse(wish.getHoliday().getName(), wish.getHoliday().getDateOfHoliday()));
        innerFeedResponse.setWishName(wish.getWishName());
        innerFeedResponse.setStatus(wish.getWishStatus());
        innerFeedResponse.setDescription(wish.getDescription());
        return innerFeedResponse;
    }

    public InnerFeedResponse getById(Long id) {
        Wish wish = wishRepository.findWishById(id).orElseThrow(() ->
                new NotFoundException("Желание с таким id: " + id + " не найдено!"));
        return mapToIdResponse(wish);
    }

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким  электронным адресом: %s не найден!", email)));
    }
}
