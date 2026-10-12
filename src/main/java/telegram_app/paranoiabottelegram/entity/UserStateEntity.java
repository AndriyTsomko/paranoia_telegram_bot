package telegram_app.paranoiabottelegram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telegram_app.paranoiabottelegram.models.BotState;

import java.util.Date;

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
    private Date deadline;

}
