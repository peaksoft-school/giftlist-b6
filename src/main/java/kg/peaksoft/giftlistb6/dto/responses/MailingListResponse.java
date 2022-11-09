package kg.peaksoft.giftlistb6.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailingListResponse {

    private Long id;
    private String name;
    private String image;
    private String text;
    private LocalDateTime createdAt;
}