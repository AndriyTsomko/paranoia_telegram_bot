package telegram_app.paranoiabottelegram.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime deadline;
}
