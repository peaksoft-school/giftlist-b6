package kg.peaksoft.giftlistb6.db.model;

import kg.peaksoft.giftlistb6.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "charity")
@Getter
@Setter
@NoArgsConstructor
public class Charity {

    @Id
    @SequenceGenerator(name = "charity_seq", sequenceName = "charity_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "charity_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "charity")
    private List<Category> category;

    @ManyToOne(cascade = CascadeType.ALL)
    private User reservoir;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "charity_status")
    private Status charityStatus;

    @Column(length = 10000)
    private String description;

    private String condition;

    @Column(length = 10000)
    private String image;

    @Column(name = "created_date")
    private LocalDate createdDate;
}
