package kg.peaksoft.giftlistb6.db.models;

import kg.peaksoft.giftlistb6.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "user_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(name = "is_block")
    private Boolean isBlock;

    private String image;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = ALL, mappedBy = "user")
    private List<Wish> wishes;

    @OneToMany(cascade = ALL, mappedBy = "user")
    private List<Gift> gifts;

    @OneToMany(cascade = ALL, mappedBy = "user")
    private List<Charity> charities;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    private List<User> friends;

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    private List<User> requests;

    @OneToMany(cascade = ALL, mappedBy = "user")
    private List<Holiday> holidays;

    @OneToMany(cascade = ALL, mappedBy = "user")
    private List<Notification> notifications;

    @OneToOne
    private UserInfo userInfo;

    public void addFriend(User friend){
        friends.add(friend);
    }

    public User(String name) {
        this.firstName = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}