package odin.zvonok.deal_service.entity.deal;

import jakarta.persistence.*;
import lombok.*;
import odin.zvonok.deal_service.entity.user.TelegramUser;
import odin.zvonok.deal_service.entity.user.User;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "deals")
@NoArgsConstructor
@AllArgsConstructor
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private float cost;

    @Column(name = "photo")
    private String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master_id", referencedColumnName = "id")
    private User master;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private User client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "telegram_user_id", referencedColumnName = "id", nullable = false)
    private TelegramUser telegramUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
