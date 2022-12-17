package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class InnerPageCharityResponse {

    private Long id;
    private String image;
    private String name;
    private String description;
    private String category;
    private String subCategory;
    private String condition;
    private LocalDate addedTime;
    private Status status;
    private Long userId;
    private String userImage;
    private String firstName;
    private String lastName;

    public InnerPageCharityResponse(Long id, String image, String name, String description, String category, String subCategory, String condition, LocalDate addedTime, Status status, Long userId, String userImage, String firstName, String lastName) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.condition = condition;
        this.addedTime = addedTime;
        this.status = status;
        this.userId = userId;
        this.userImage = userImage;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
