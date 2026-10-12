package telegram_app.paranoiabottelegram.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class Homework {
    private Long id;
    private DisciplineLecturer disciplineLecturer;
    private LocalDateTime createdTime;
    private String title;
    private String description;
    private Long telegramId;
    private String fileId;
    private Date deadline;
}
