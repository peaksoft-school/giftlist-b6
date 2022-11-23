package kg.peaksoft.giftlistb6.db.models;

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
    @SequenceGenerator(name = "gift_gen", sequenceName = "gift_seq", allocationSize = 1, initialValue = 15)
    @GeneratedValue(generator = "gift_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private User user;

    @OneToOne(cascade = {CascadeType.REFRESH,CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private Wish wish;
}