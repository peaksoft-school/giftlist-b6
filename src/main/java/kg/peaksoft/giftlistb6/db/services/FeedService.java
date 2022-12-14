package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.MailingListRepository;
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

    private final MailingListRepository mailingListRepository;

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким  электронным адресом: %s не найден!", email)));
    }

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
        if (wish.getHoliday() != null) {
            feedResponse.setHoliday(new HolidayResponse(wish.getHoliday().getId(), wish.getHoliday().getName(), wish.getDateOfHoliday()));
        } else {
            feedResponse.setHoliday(new HolidayResponse());
        }
        if (wish.getReservoir() == null) {
            feedResponse.setUserFeedResponse(new UserFeedResponse());
        } else {
            feedResponse.setUserFeedResponse(new UserFeedResponse(wish.getReservoir().getId(), wish.getReservoir().getImage()));
        }
        return feedResponse;
    }

    public AllFeedResponse feedResponse() {
        User user = getAuthPrincipal();
        AllFeedResponse allFeedResponse = new AllFeedResponse();
        allFeedResponse.setMailingLists(mailingListRepository.findAllMailingList());
        allFeedResponse.setFeeds(convertAllToResponse(wishRepository.getAllWishes(user.getEmail())));
        return allFeedResponse;
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
        return responses;
    }

    public Deque<FeedResponse> getAll() {
        User user = getAuthPrincipal();
        return convertAllToResponse(wishRepository.getAllWishes(user.getEmail()));
    }

    public InnerFeedResponse getById(Long id) {
        Wish wish = wishRepository.findWishById(id).orElseThrow(() -> {
            log.error("Wish with id: {} not found!", id);
            throw new NotFoundException("Желание с таким id: " + id + " не найдено!");
        });
        return new InnerFeedResponse(wish);
    }
}