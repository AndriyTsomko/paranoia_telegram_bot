package telegram_app.paranoiabottelegram.repository.mapper;

import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.entity.LecturerEntity;
import telegram_app.paranoiabottelegram.models.Lecturer;

@Component
public class LecturerMapper implements MapperInterface<Lecturer, LecturerEntity> {
    @Override
    public LecturerEntity toEntity(Lecturer model) {
        if (model == null) {
            return null;
        }
        return LecturerEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    @Override
    public Lecturer toModel(LecturerEntity entity) {
        if (entity == null) {
            return null;
        }
        return Lecturer.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
