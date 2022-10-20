package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.db.model.Category;
import kg.peaksoft.giftlistb6.db.model.User;
import kg.peaksoft.giftlistb6.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CharityResponse {

    private Long id;
    private String name;
    private Status charityStatus;
    private String description;
    private String condition;
    private String image;
    private LocalDate createdDate;
}
