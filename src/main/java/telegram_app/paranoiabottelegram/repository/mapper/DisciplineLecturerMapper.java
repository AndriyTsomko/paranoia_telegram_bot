package telegram_app.paranoiabottelegram.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.entity.DisciplineLecturerEntity;
import telegram_app.paranoiabottelegram.models.DisciplineLecturer;

@Component
@AllArgsConstructor
public class DisciplineLecturerMapper implements MapperInterface<DisciplineLecturer, DisciplineLecturerEntity> {
    private final DisciplineMapper disciplineMapper;
    private final LecturerMapper lecturerMapper;

    @Override
    public DisciplineLecturerEntity toEntity(DisciplineLecturer model) {
        if (model == null) {
            return null;
        }
        return DisciplineLecturerEntity.builder()
                .id(model.getId())
                .discipline(disciplineMapper.toEntity(model.getDiscipline()))
                .lecturer(lecturerMapper.toEntity(model.getLecturer()))
                .build();
    }

    @Override
    public DisciplineLecturer toModel(DisciplineLecturerEntity entity) {
        if (entity == null) {
            return null;
        }
        return DisciplineLecturer.builder()
                .id(entity.getId())
                .discipline(disciplineMapper.toModel(entity.getDiscipline()))
                .lecturer(lecturerMapper.toModel(entity.getLecturer()))
                .build();
    }
}
