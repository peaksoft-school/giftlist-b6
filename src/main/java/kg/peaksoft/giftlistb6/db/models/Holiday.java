package kg.peaksoft.giftlistb6.db.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "holidays")
@Getter
@Setter
@NoArgsConstructor
public class Holiday {

    @Id
    @SequenceGenerator(name = "holiday_seq", sequenceName = "holiday_seq", allocationSize = 1, initialValue = 15)
    @GeneratedValue(generator = "holiday_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Column(name = "date_of_holiday")
    private LocalDate dateOfHoliday;

    @Column(length = 10000)
    private String image;

    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "holiday")
    private List<Wish> wishes;
}
