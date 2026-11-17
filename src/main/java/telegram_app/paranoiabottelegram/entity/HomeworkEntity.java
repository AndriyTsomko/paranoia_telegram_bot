package telegram_app.paranoiabottelegram.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "homeworks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeworkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_lecturer_id",referencedColumnName = "id", nullable = false)
    private DisciplineLecturerEntity disciplineLecturer;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @OneToMany(mappedBy = "homework", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<FileEntity> files;

}
