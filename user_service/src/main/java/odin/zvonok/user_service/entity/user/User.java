package odin.zvonok.user_service.entity.user;

import jakarta.persistence.*;
import lombok.*;
import odin.zvonok.user_service.entity.deal.Deal;
import odin.zvonok.user_service.entity.role.Role;
import odin.zvonok.user_service.entity.tariff.Tariff;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_id", unique = true)
    private String telegramId;

    @Column(name = "telegram_username")
    private String telegramUsername;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AuthCredentials authCredentials;

    @Column(name = "average_rating")
    private Double averageRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_status")
    private MasterActivityStatus activityStatus;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Tariff tariff;

    @OneToMany(mappedBy = "client")
    private List<Deal> clientDeals;

    @OneToMany(mappedBy = "master")
    private List<Deal> masterDeals;

    @ManyToMany
    @JoinTable(
            name = "master_categories",
            joinColumns = @JoinColumn(name = "master_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<MasterCategory> categories;

}