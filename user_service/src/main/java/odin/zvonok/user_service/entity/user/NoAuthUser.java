package odin.zvonok.user_service.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odin.zvonok.user_service.entity.deal.Deal;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "noauth_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoAuthUser {
    @Id
    @Column(name = "telegram_id")
    private String telegramId;

    @Column(name = "telegram_username")
    private String telegramUsername;

    @Column(name = "first_seen")
    private LocalDateTime firstSeen;

    @OneToMany(mappedBy = "noAuthClient")
    private List<Deal> deals;
}