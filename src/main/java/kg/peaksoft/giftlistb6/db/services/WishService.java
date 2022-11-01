package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Holiday;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.HolidayRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.requests.WishRequest;
import kg.peaksoft.giftlistb6.dto.responses.HolidayResponse;
import kg.peaksoft.giftlistb6.dto.responses.InnerWishResponse;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.dto.responses.WishResponse;
import kg.peaksoft.giftlistb6.enums.Status;
import kg.peaksoft.giftlistb6.exceptions.BadRequestException;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;

    private final HolidayRepository holidayRepository;

    private final UserRepository userRepository;

    public User getAuthPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("user with email %s not found", email)));
    }

    public WishResponse saveWish(WishRequest wishRequest) {
        Wish wish = mapToEntity(wishRequest);
        Holiday holiday = holidayRepository.findById(wishRequest.getHolidayId()).orElseThrow(
                () -> new NotFoundException("not found")
        );
        holiday.addWish(wish);
        wish.setHoliday(holiday);
        wish.setWishStatus(Status.WAIT);
        wishRepository.save(wish);
        return mapToResponse(wish);
    }

    public Wish mapToEntity(WishRequest wishRequest) {
        Wish wish = new Wish();
        User user = getAuthPrincipal();
        wish.setUser(user);
        wish.setWishName(wishRequest.getWishName());
        wish.setDescription(wishRequest.getDescription());
        Holiday holiday = holidayRepository.findById(wishRequest.getHolidayId())
                .orElseThrow(() -> new NotFoundException("not found"));
        if (!wishRequest.getDateOfHoliday().equals(holiday.getDateOfHoliday())) {
            throw new BadRequestException("incorrect data of holiday");
        }
        wish.setDateOfHoliday(holiday.getDateOfHoliday());
        wish.setImage(wishRequest.getImage());
        wish.setLinkToGift(wishRequest.getLinkToGift());
        return wish;
    }

    public WishResponse mapToResponse(Wish wish) {
        WishResponse response = new WishResponse();
        response.setId(wish.getId());
        response.setWishName(wish.getWishName());
        response.setImage(wish.getImage());
        response.setHoliday(
                new HolidayResponse(wish.getHoliday().getName(), wish.getHoliday().getDateOfHoliday()));
        response.setWishStatus(wish.getWishStatus());
        return response;
    }

    public WishResponse update(Long id, WishRequest wishRequest) {
        Wish wish = getById(id);
        convertToUpdate(wish, wishRequest);
        wishRepository.save(wish);
        return mapToResponse(wish);
    }

    public SimpleResponse deleteWishById(Long id) {
        boolean exists = wishRepository.existsById(id);
        if (!exists) {
            throw new NotFoundException("wish with id " + id + " not found!");
        }
        wishRepository.deleteById(id);
        return new SimpleResponse(
                "DELETED",
                "wish with id " + id + "deleted successfully");
    }

    public InnerWishResponse findById(Long id) {
        Wish wish = getById(id);
        return mapToInnerResponse(wish);
    }

    public List<WishResponse> findAll() {
        return convertAllToResponse(wishRepository.findAll());
    }

    public InnerWishResponse mapToInnerResponse(Wish wish) {
        InnerWishResponse innerWishResponse = new InnerWishResponse();
        innerWishResponse.setId(wish.getId());
        innerWishResponse.setWishName(wish.getWishName());
        innerWishResponse.setLinkToGift(wish.getLinkToGift());
        innerWishResponse.setHoliday(new HolidayResponse(wish.getHoliday().getName(), wish.getHoliday().getDateOfHoliday()));
        innerWishResponse.setDescription(wish.getDescription());
        return innerWishResponse;
    }

    public Wish convertToUpdate(Wish wish, WishRequest wishRequest) {
        wish.setWishName(wishRequest.getWishName());
        wish.setImage(wishRequest.getImage());
        wish.setLinkToGift(wishRequest.getLinkToGift());
        return wish;
    }

    public List<WishResponse> convertAllToResponse(List<Wish> wishes) {
        List<WishResponse> wishResponses = new ArrayList<>();
        for (Wish wish : wishes) {
            wishResponses.add(mapToResponse(wish));
        }
        return wishResponses;
    }


    private Wish getById(Long id) {
        return wishRepository.findById(id).orElseThrow(() ->
                new NotFoundException("wish with id: " + id + " not found!"));
    }
}
