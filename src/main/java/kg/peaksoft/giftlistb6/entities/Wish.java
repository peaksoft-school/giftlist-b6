package kg.peaksoft.giftlistb6.entities;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "wishes")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Wish {
    @Id
    @SequenceGenerator(name = "wish_seq", sequenceName = "wish_seq", allocationSize = 1)
    @GeneratedValue(generator = "wish_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "wish_name")
    private String wishName;

    @Column(name = "link_to_gift")
    private String linkToGift;

    @Column(name = "date_of_holiday")
    private LocalDate dateOfHoliday;

    private String description;

    @Column(length = 100000)
    private String image;

    @Column(name = "wish_status")
    @Enumerated(EnumType.STRING)
    private Status wishStatus;

    @OneToOne
    private User reservoir;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wish")
    private List<Complaint> complaints;
    @ManyToOne(cascade = {CascadeType.REFRESH
            , CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    private User user;

    @ManyToOne(cascade = {CascadeType.REFRESH
            , CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    private Holiday holiday;

}
