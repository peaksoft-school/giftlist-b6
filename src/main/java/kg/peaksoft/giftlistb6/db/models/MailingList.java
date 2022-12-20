package kg.peaksoft.giftlistb6.db.models;

import kg.peaksoft.giftlistb6.dto.requests.MailingListRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mailing_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailingList {

    @Id
    @SequenceGenerator(name = "mailingList_gen", sequenceName = "mailingList_seq", allocationSize = 1)
    @GeneratedValue(generator = "mailingList_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String image;

    @Column(length = 10000)
    private String text;

    private LocalDateTime createdAt;

    public MailingList(MailingListRequest mailingListRequest) {
        this.name = mailingListRequest.getName();
        this.image = mailingListRequest.getName();
        this.text = mailingListRequest.getText();
        this.createdAt = LocalDateTime.now();

    }
}
