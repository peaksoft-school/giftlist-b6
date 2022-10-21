package kg.peaksoft.giftlistb6.db.models;

import kg.peaksoft.giftlistb6.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1, initialValue = 5)
    @GeneratedValue(generator = "user_seq", strategy = GenerationType.SEQUENCE)
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

    private String photo;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Wish> wishes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Gift> gifts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Charity> charities;

    @OneToMany(cascade = CascadeType.ALL)
    private List<User> friends;

    @OneToMany(cascade = CascadeType.ALL)
    private List<User> requests;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Holiday> holidays;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Notification> notifications;

    @OneToOne
    private UserInfo userInfo;

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