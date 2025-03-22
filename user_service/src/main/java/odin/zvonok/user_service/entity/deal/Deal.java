package odin.zvonok.user_service.entity.deal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odin.zvonok.user_service.entity.user.NoAuthUser;
import odin.zvonok.user_service.entity.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "noauth_client_id")
    private NoAuthUser noAuthClient;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private User master;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    private DealStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    public enum DealStatus {
        CREATED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}