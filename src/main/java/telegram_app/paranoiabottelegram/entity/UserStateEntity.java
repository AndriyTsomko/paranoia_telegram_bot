package telegram_app.paranoiabottelegram.entity;

import jakarta.persistence.*;
import lombok.*;
import telegram_app.paranoiabottelegram.models.BotState;

import java.time.LocalDateTime;
import java.util.List;

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
    @Column(name = "chat_id")
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

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @OneToMany(mappedBy = "userState", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<FileEntity> files;
}
