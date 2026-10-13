package telegram_app.paranoiabottelegram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "deadline", nullable = false)
    private Date deadline;
}
