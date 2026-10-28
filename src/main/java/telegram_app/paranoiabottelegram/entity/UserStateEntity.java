package telegram_app.paranoiabottelegram.entity;

import jakarta.persistence.*;
import lombok.*;
import telegram_app.paranoiabottelegram.models.BotState;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_states")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
public class UserStateEntity {
    @Id
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private BotState state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_lecturer_id")
    private DisciplineLecturerEntity disciplineLecturer;

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "deadline")
    private LocalDateTime deadline;

}
