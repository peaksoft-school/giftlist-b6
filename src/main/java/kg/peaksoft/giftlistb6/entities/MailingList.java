package kg.peaksoft.giftlistb6.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "mailing_list")
@Getter
@Setter
@NoArgsConstructor
public class MailingList {

    @Id
    @SequenceGenerator(name = "mailingList_gen", sequenceName = "mailingList_gen", allocationSize = 1)
    @GeneratedValue(generator = "mailingList_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Column(length = 10000)
    private String text;
}
