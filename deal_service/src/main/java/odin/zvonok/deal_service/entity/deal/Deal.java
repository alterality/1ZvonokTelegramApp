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
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private float cost;

    @Column(name = "photo")
    private String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private User masterId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private User clientId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private TelegramUser telegramUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
