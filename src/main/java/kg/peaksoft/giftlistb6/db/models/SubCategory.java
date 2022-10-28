package kg.peaksoft.giftlistb6.db.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {

    @Id
    @SequenceGenerator(name = "subCategory_seq", sequenceName = "subCategory_seq", allocationSize = 1, initialValue = 15)
    @GeneratedValue(generator = "subCategory_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    @OneToOne (cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH},mappedBy = "subCategory")
    private Charity charity;

    public SubCategory(String name, Category category) {
        this.name = name;
        this.category = category;
    }
}
