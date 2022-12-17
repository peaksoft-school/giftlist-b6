package kg.peaksoft.giftlistb6.db.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    @Id
    @SequenceGenerator(name = "userInfo_gen", sequenceName = "userInfo_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "userInfo_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String image;

    private String country;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "shoe_size")
    private int shoeSize;

    @Column(name = "clothing_size")
    private String clothingSize;

    private String hobby;

    private String important;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, mappedBy = "userInfo")
    private User user;

    private String facebookLink;
    private String instagramLink;
    private String telegramLink;
    private String vkLink;
}