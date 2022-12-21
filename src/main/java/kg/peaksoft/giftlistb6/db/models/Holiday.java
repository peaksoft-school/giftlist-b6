package kg.peaksoft.giftlistb6.db.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Holiday {

    @Id
    @SequenceGenerator(name = "holiday_gen", sequenceName = "holiday_seq", allocationSize = 1, initialValue = 20)
    @GeneratedValue(generator = "holiday_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Column(name = "date_of_holiday")
    private LocalDate dateOfHoliday;

    @Column(length = 10000)
    private String image;

    @Column(name = "is_block")
    private Boolean isBlock;

    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "holiday")
    @JsonIgnore
    private List<Wish> wishes;

    public void addWish(Wish wish) {
        this.wishes.add(wish);
    }
}