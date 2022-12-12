package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Complaint;
import kg.peaksoft.giftlistb6.db.models.Notification;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.db.repositories.ComplaintRepository;
import kg.peaksoft.giftlistb6.db.repositories.NotificationRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.db.repositories.WishRepository;
import kg.peaksoft.giftlistb6.dto.requests.ComplaintRequest;
import kg.peaksoft.giftlistb6.dto.responses.ComplaintResponseForAdmin;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import kg.peaksoft.giftlistb6.enums.NotificationType;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ComplaintsService {

    private final ComplaintRepository complaintRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public User getPrinciple() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("Жалоба с таким id  = %s не найдено", email))
        );
    }

    public SimpleResponse creatComplain(ComplaintRequest request) {
        Complaint complaint = convertToEntity(request);
        complaintRepository.save(complaint);
        log.info("Complaint send successfully ");
        return new SimpleResponse("Жалоба успешно отправлен", "ok");
    }

    public Complaint convertToEntity(ComplaintRequest request) {
        Wish wish = wishRepository.findById(request.getWishId()).orElseThrow(
                () -> new NotFoundException(String.format("Жалоба с таким id  = %s не найдено", request.getWishId()))
        );
        User user = getPrinciple();
        Complaint complaint = new Complaint();
        complaint.setReasonText(request.getComplaintText());
        complaint.setCreatedAt(LocalDate.now());
        complaint.setComplainer(user);
        complaint.setIsSeen(false);
        complaint.setWish(wish);
        Notification notification = new Notification();
        notification.setComplaints(List.of(complaint));
        notification.setCreatedDate(LocalDate.now());
        notification.setNotificationType(NotificationType.CREATE_COMPLAINTS);
        notification.setIsSeen(false);
        notification.setFromUser(user);
        notification.setUser(wish.getUser());
        notification.setWish(wish);
        notificationRepository.save(notification);
        return complaint;
    }

    public List<ComplaintResponseForAdmin> getAllComplaints() {
        log.info("Admin seen complaints");
        return complaintRepository.getAllComplaints();
    }

    public ComplaintResponseForAdmin getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Жалоба с таким id  = %s не найдено", id))
        );
        return convertToResponse(complaint);
    }

    public SimpleResponse blockWishByIdFromComplaint(Long id) {
        Wish wish = wishRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Желания с таким id = %s не найдено", id))
        );
        wish.setIsBlock(true);
        log.info("Wish with id: {} successfully blocked ", id);
        return new SimpleResponse("Заблокирован", "ok");
    }

    public SimpleResponse unBlockWishByIdFromComplaint(Long id) {
        Wish wish = wishRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Желания с таким id = %s не найдено", id))
        );
        wish.setIsBlock(false);
        log.info("Wish with id: {} successfully unblocked ", id);
        return new SimpleResponse("Разблокирован", "ok");
    }

    public ComplaintResponseForAdmin convertToResponse(Complaint complaint) {
        ComplaintResponseForAdmin complaintResponseForAdmin = new ComplaintResponseForAdmin();
        complaintResponseForAdmin.setId(complaint.getId());
        complaintResponseForAdmin.setUserId(complaint.getWish().getUser().getId());
        complaintResponseForAdmin.setUserPhoto(complaint.getWish().getUser().getImage());
        complaintResponseForAdmin.setUserPhoneNumber(complaint.getWish().getUser().getUserInfo().getPhoneNumber());
        complaintResponseForAdmin.setFirstName(complaint.getWish().getUser().getFirstName());
        complaintResponseForAdmin.setLastName(complaint.getWish().getUser().getLastName());
        complaintResponseForAdmin.setHolidayName(complaint.getWish().getHoliday().getName());
        complaintResponseForAdmin.setWishName(complaint.getWish().getWishName());
        complaintResponseForAdmin.setWishPhoto(complaint.getWish().getImage());
        complaintResponseForAdmin.setCreatedAt(complaint.getCreatedAt());
        complaintResponseForAdmin.setComplainerId(complaint.getComplainer().getId());
        complaintResponseForAdmin.setComplainerPhoto(complaint.getComplainer().getImage());
        complaintResponseForAdmin.setReason(complaint.getReasonText());
        return complaintResponseForAdmin;
    }
}