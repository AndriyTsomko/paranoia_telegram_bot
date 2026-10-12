package telegram_app.paranoiabottelegram.repository.mapper;

import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.entity.DisciplineEntity;
import telegram_app.paranoiabottelegram.models.Discipline;

@Component
public class DisciplineMapper implements MapperInterface<Discipline, DisciplineEntity> {
    @Override
    public DisciplineEntity toEntity(Discipline model) {
        if (model == null) {
            return null;
        }
        return DisciplineEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    @Override
    public Discipline toModel(DisciplineEntity entity) {
        if (entity == null) {
            return null;
        }
        return Discipline.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
