package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
