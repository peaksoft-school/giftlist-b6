package kg.peaksoft.giftlistb6.db.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@NoArgsConstructor
public class Complaint {

    @Id
    @SequenceGenerator(name = "complaint_gen", sequenceName = "complaint_seq", allocationSize = 1, initialValue = 12)
    @GeneratedValue(generator = "complaint_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private Wish wish;

    @ManyToOne(cascade = CascadeType.ALL)
    private User complainer;

    @CreatedDate
    private LocalDate createdAt;

    private Boolean isSeen;

    @Column(length = 10000)
    private String reasonText;
}