package telegram_app.paranoiabottelegram.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.entity.HomeworkEntity;
import telegram_app.paranoiabottelegram.models.Homework;

@Component
@AllArgsConstructor
public class HomeworkMapper implements MapperInterface<Homework, HomeworkEntity> {
    private final DisciplineLecturerMapper disciplineLecturerMapper;
    @Override
    public HomeworkEntity toEntity(Homework model) {
        if (model == null) {
            return null;
        }
        return HomeworkEntity.builder()
                .id(model.getId())
                .disciplineLecturer(disciplineLecturerMapper.toEntity(model.getDisciplineLecturer()))
                .createdTime(model.getCreatedTime())
                .title(model.getTitle())
                .description(model.getDescription())
                .telegramId(model.getTelegramId())
                .fileId(model.getFileId())
                .deadline(model.getDeadline())
                .build();
    }

    @Override
    public Homework toModel(HomeworkEntity entity) {
        if (entity == null) {
            return null;
        }
        return Homework.builder()
                .id(entity.getId())
                .disciplineLecturer(disciplineLecturerMapper.toModel(entity.getDisciplineLecturer()))
                .createdTime(entity.getCreatedTime())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .telegramId(entity.getTelegramId())
                .fileId(entity.getFileId())
                .deadline(entity.getDeadline())
                .build();
    }
}
