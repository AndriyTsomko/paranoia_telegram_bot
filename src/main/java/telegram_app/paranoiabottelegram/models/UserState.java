package telegram_app.paranoiabottelegram.models;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
public class UserState {
    private Long chatId;
    private BotState state;
    private DisciplineLecturer disciplineLecturer;
    private String title;
    private String description;
    private String fileId;
    private LocalDateTime deadline;

    public void clear() {
        this.setState(BotState.IDLE);
        this.setDisciplineLecturer(null);
        this.setDeadline(null);
        this.setDescription(null);
        this.setTitle(null);
        this.setFileId(null);
    }

    public Homework toHomeworkDraft(){
        return Homework.builder()
                .disciplineLecturer(this.disciplineLecturer)
                .title(this.title)
                .description(this.description)
                .telegramId(this.chatId)
                .fileId(this.fileId)
                .deadline(this.deadline)
                .build();
    }
}
