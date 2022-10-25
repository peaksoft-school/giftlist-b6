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
public class SubCategory {

    @Id
    @SequenceGenerator(name = "subCategory_seq", sequenceName = "subCategory_seq", allocationSize = 1, initialValue = 15)
    @GeneratedValue(generator = "subCategory_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;
}
