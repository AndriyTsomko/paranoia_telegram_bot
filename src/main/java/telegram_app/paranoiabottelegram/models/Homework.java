package telegram_app.paranoiabottelegram.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import telegram_app.paranoiabottelegram.entity.FileEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Homework {
    public List<FileEntity> g;
    private Long id;
    private DisciplineLecturer disciplineLecturer;
    private LocalDateTime createdTime;
    private String title;
    private String description;
    private Long telegramId;
    private LocalDateTime deadline;
    private List<File> files;
}
