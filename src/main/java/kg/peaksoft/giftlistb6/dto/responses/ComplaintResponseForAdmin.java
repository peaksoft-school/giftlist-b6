package kg.peaksoft.giftlistb6.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ComplaintResponseForAdmin {

    private Long id;
    private Long userId;
    private String userPhoto;
    private String userPhoneNumber;
    private String firstName;
    private String lastName;
    private String holidayName;
    private String wishName;
    private String wishPhoto;
    private LocalDate createdAt;
    private Long complainerId;
    private String complainerPhoto;
    private String reason;
    private Boolean isBLock;

    public ComplaintResponseForAdmin(Long id, Long userId, String userPhoto, String userPhoneNumber, String firstName, String lastName, String holidayName, String wishName, String wishPhoto, LocalDate createdAt, Long complainerId, String complainerPhoto,Boolean isBlock) {
        this.id = id;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.userPhoneNumber = userPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.holidayName = holidayName;
        this.wishName = wishName;
        this.wishPhoto = wishPhoto;
        this.createdAt = createdAt;
        this.complainerId = complainerId;
        this.complainerPhoto = complainerPhoto;
        this.isBLock = isBlock;
    }
}