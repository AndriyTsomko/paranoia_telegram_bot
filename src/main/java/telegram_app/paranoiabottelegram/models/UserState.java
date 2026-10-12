package telegram_app.paranoiabottelegram.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telegram_app.paranoiabottelegram.entity.DisciplineLecturerEntity;

import java.util.Date;

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
    private Date deadline;
}
