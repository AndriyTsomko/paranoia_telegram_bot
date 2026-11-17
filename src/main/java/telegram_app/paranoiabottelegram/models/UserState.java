package telegram_app.paranoiabottelegram.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime deadline;
    @Builder.Default
    private List<File> files = new ArrayList<>();

    public void clear() {
        this.setState(BotState.IDLE);
        this.setDisciplineLecturer(null);
        this.setDeadline(null);
        this.setDescription(null);
        this.setTitle(null);
        this.setFiles(new ArrayList<>());
    }

    public Homework toHomeworkDraft(){
        return Homework.builder()
                .disciplineLecturer(this.disciplineLecturer)
                .title(this.title)
                .description(this.description)
                .telegramId(this.chatId)
                .deadline(this.deadline)
                .files(this.files)
                .build();
    }
}
