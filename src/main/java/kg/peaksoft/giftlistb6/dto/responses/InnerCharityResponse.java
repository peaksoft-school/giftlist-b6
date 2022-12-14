package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InnerCharityResponse {

    private Long id;
    private String image;
    private String name;
    private String description;
    private String category;
    private String subCategory;
    private String condition;
    private LocalDate addedTime;
    private Status status;
    private Boolean isBlock;
    private UserCharityResponse userCharityResponse;
    private ReservoirResponse reservoirResponse;

    public InnerCharityResponse(Long id, String image, String name, String description, String category, String subCategory, String condition, LocalDate addedTime, Status status) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.condition = condition;
        this.addedTime = addedTime;
        this.status = status;
    }

    public InnerCharityResponse(Long id, String image, String name, String description, String category, String subCategory, String condition, LocalDate addedTime, Status status, Boolean isBlock) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.condition = condition;
        this.addedTime = addedTime;
        this.status = status;
        this.isBlock = isBlock;
    }
}
