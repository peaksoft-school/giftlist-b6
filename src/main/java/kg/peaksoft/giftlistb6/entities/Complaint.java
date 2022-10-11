package kg.peaksoft.giftlistb6.entities;

import kg.peaksoft.giftlistb6.enums.Reason;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@NoArgsConstructor
public class Complaint {
    @Id
    @SequenceGenerator(name = "complaint_seq", sequenceName = "complaint_seq", allocationSize = 1)
    @GeneratedValue(generator = "complaint_seq", strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private Wish wish;

    @ManyToOne
    private User complainer;

    private Boolean seen;

    @Enumerated
    @Column(name = "reason_of_complaint")
    private Reason reasonOfComplaint;
}
