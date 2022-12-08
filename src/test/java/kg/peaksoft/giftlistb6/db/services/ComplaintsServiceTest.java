package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Complaint;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.ComplaintRepository;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest
class ComplaintsServiceTest {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ComplaintsService complaintsService;

    @Test
    void creatComplain() {
        Complaint complaint = new Complaint(1L,
                new Wish(),
                new User(), LocalDate.of(2022, 10, 10), true, "reasonText");
        Assertions.assertThat(complaint.getId()).isGreaterThan(0);

    }

    @Test
    void getAllComplaints() {
        int size = complaintRepository.getAllComplaints().size();
        Assertions.assertThat(size).isGreaterThan(0);
    }

    @Test
    void getComplaintById() {
        Complaint complaint = complaintRepository.getReferenceById(1L);
        Assertions.assertThat(complaint.getId()).isEqualTo(1L);

    }

    @Test
    void blockWishByIdFromComplaint() {
        Wish wish = wishRepository.findWishById(1L).orElseThrow(
                () -> new NotFoundException("Wish not found"));
        boolean block = wish.getIsBlock();

        SimpleResponse simpleResponse = complaintsService.blockWishByIdFromComplaint(1L);

        Wish wish2 = wishRepository.findWishById(1L).orElseThrow(
                () -> new NotFoundException("Wish not found"));
        boolean block2 = wish2.getIsBlock();

        Assertions.assertThat(simpleResponse).info.description("Заблокирован");
        assertNotEquals(block, block2);
    }

    @Test
    void unBlockWishByIdFromComplaint() {
        SimpleResponse simpleResponse = complaintsService.unBlockWishByIdFromComplaint(1L);
        Wish wish1 = wishRepository.findWishById(1L).orElseThrow(
                () -> new NotFoundException("Wish not found"));
        Assertions.assertThat(simpleResponse).info.description("Разблокирован");
        assertEquals(false, wish1.getIsBlock());
    }
}