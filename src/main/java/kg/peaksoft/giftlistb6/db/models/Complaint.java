package kg.peaksoft.giftlistb6.db.models;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {

    @Id
    @SequenceGenerator(name = "complaint_gen", sequenceName = "complaint_seq", allocationSize = 1, initialValue = 12)
    @GeneratedValue(generator = "complaint_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = {REFRESH, PERSIST, MERGE, DETACH})
    private Wish wish;

    @ManyToOne(cascade = CascadeType.ALL)
    private User complainer;

    @ManyToOne(cascade = ALL)
    private Notification notification;

    @CreatedDate
    private LocalDate createdAt;

    private Boolean isSeen;

    @Column(length = 10000)
    private String reasonText;

    public Complaint(Long id, Wish wish, User complainer, LocalDate createdAt, Boolean isSeen, String reasonText) {
        this.id = id;
        this.wish = wish;
        this.complainer = complainer;
        this.createdAt = createdAt;
        this.isSeen = isSeen;
        this.reasonText = reasonText;
    }
}