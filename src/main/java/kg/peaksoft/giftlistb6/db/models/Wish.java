package kg.peaksoft.giftlistb6.db.models;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "wishes")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class Wish {

    @Id
    @SequenceGenerator(name = "wish_seq", sequenceName = "wish_seq", allocationSize = 1, initialValue = 6)
    @GeneratedValue(generator = "wish_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "wish_name")
    private String wishName;

    @Column(name = "link_to_gift")
    private String linkToGift;

    @Column(name = "date_of_holiday")
    private LocalDate dateOfHoliday;

    @Column(length = 10000)
    private String description;

    @Column(length = 10000)
    private String image;

    @Column(name = "wish_status")
    @Enumerated(EnumType.STRING)
    private Status wishStatus;

    @Column(name = "is_block")
    private Boolean isBlock;

    @OneToOne(cascade = CascadeType.ALL)
    private Gift gift;

    @OneToOne
    private User reservoir;

    @OneToMany(cascade = ALL, mappedBy = "wish")
    private List<Complaint> complaints;

    @ManyToOne(cascade = {REFRESH, DETACH, MERGE, PERSIST})
    private User user;

    @ManyToOne(cascade = {REFRESH, DETACH, MERGE, PERSIST})
    private Holiday holiday;
}