package kg.peaksoft.giftlistb6.db.models;

import kg.peaksoft.giftlistb6.enums.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @SequenceGenerator(name = "notification_seq", sequenceName = "notification_seq", allocationSize = 1)
    @GeneratedValue(generator = "notification_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "created_date")
    private LocalDate createdDate;

    private Boolean isSeen;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @OneToOne
    private User requestToFriend;

    @OneToOne
    private Wish wish;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Complaint> complaints;

    @OneToOne
    private Gift gift;
}
