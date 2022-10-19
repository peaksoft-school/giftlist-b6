package kg.peaksoft.giftlistb6.db.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1, initialValue = 15)
    @GeneratedValue(generator = "category_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<SubCategory> subCategory;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
    private Charity charity;
}
