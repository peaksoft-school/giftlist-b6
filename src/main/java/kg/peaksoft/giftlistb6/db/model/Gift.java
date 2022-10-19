package kg.peaksoft.giftlistb6.db.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Gift {

    @Id
    @SequenceGenerator(name = "gift_seq", sequenceName = "gift_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "gift_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private User user;

    @OneToOne
    private Wish wish;
}
