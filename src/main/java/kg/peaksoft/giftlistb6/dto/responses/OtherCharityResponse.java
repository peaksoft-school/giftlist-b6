package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class OtherCharityResponse {

    private Long id;
    private String image;
    private String name;
    private String condition;
    private LocalDate addedDate;
    private Status status;
    private Long userId;
    private String fullName;
    private String photo;
    private ReservoirResponse reservoir;

    public OtherCharityResponse(Long id, String image, String name, String condition, LocalDate addedDate, Status status, Long userId, String fullName, String photo) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.condition = condition;
        this.addedDate = addedDate;
        this.status = status;
        this.userId = userId;
        this.fullName = fullName;
        this.photo = photo;
    }
}
