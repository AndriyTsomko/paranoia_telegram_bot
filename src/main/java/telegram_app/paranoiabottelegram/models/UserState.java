package telegram_app.paranoiabottelegram.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public void clear(){
        this.setState(BotState.IDLE);
        this.setDisciplineLecturer(null);
        this.setDeadline(null);
        this.setDescription(null);
        this.setTitle(null);
        this.setFileId(null);
    }
}
