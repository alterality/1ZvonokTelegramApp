package odin.zvonok.feedback_service.entity.rating;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odin.zvonok.feedback_service.entity.user.TelegramUser;
import odin.zvonok.feedback_service.entity.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rate", nullable = false)
    @Min(value = 1)
    @Max(value = 5)
    private int rate;

    @Column(name = "review")
    @Size(max = 500)
    private String review;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master_id", referencedColumnName = "id", nullable = false)
    private User master;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private User client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "telegram_user_id", referencedColumnName = "id", nullable = false)
    private TelegramUser telegramUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "average_rating_id", referencedColumnName = "id", nullable = false)
    private AverageRating averageRatingId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
