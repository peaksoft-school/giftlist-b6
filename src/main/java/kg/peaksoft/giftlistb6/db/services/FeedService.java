package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final WishRepository wishRepository;

    public FeedResponse mapToAllResponse(Wish wish) {
        FeedResponse feedResponse = new FeedResponse();
        feedResponse.setId(wish.getId());
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


    public List<FeedResponse> convertAllToResponse(List<Wish> wishes) {
        List<FeedResponse> responses = new ArrayList<>();
        for (Wish wish : wishes) {
            responses.add(mapToAllResponse(wish));
        }
        return responses;
    }

    public List<FeedResponse> getAll() {
        return convertAllToResponse(wishRepository.findAll());
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
        Wish wish = wishRepository.findById(id).orElseThrow(() ->
                new NotFoundException("wish with id: " + id + " not found!"));
        return mapToIdResponse(wish);
    }
}
