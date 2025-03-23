package odin.zvonok.feedback_service.entity.rating;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odin.zvonok.feedback_service.entity.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "average_rating")
public class AverageRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "average", nullable = false)
    private float average;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master_id", referencedColumnName = "id", nullable = false)
    private User master;

    @OneToMany(mappedBy = "averageRatingId")
    private List<Rating> ratings;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
