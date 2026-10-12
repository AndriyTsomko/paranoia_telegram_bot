
package telegram_app.paranoiabottelegram.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telegram_app.paranoiabottelegram.entity.DisciplineLecturerEntity;
import telegram_app.paranoiabottelegram.models.DisciplineLecturer;
import telegram_app.paranoiabottelegram.repository.jpa.JpaDisciplineLecturerRepository;
import telegram_app.paranoiabottelegram.repository.mapper.DisciplineLecturerMapper;
import telegram_app.paranoiabottelegram.repository.mapper.DisciplineMapper;
import telegram_app.paranoiabottelegram.repository.mapper.LecturerMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisciplineLecturerService implements
        ServiceInterface<DisciplineLecturer, DisciplineLecturerEntity, Long> {

    private final JpaDisciplineLecturerRepository repository;
    private final DisciplineLecturerMapper disciplineLecturerMapper;
    private final DisciplineMapper disciplineMapper;
    private final LecturerMapper lecturerMapper;

    @Override
    public DisciplineLecturerEntity save(DisciplineLecturer disciplineLecturer) {
        return repository.save(disciplineLecturerMapper.toEntity(disciplineLecturer));
    }

    @Override
    public void delete(DisciplineLecturer disciplineLecturer) {
        repository.delete(disciplineLecturerMapper.toEntity(disciplineLecturer));
    }

    @Override
    public void update(DisciplineLecturer modelToUpdate, DisciplineLecturer newModel) {
        DisciplineLecturerEntity entity = repository.findById(modelToUpdate.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format(
                                "Зв'язок дисципліни з викладачем з id %d не знайдено",
                                modelToUpdate.getId()
                        )
                ));

        entity.setDiscipline(disciplineMapper.toEntity(newModel.getDiscipline()));
        entity.setLecturer(lecturerMapper.toEntity(newModel.getLecturer()));

        repository.save(entity);
    }

    @Override
    public List<DisciplineLecturerEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<DisciplineLecturerEntity> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<DisciplineLecturerEntity> findByDiscipline(String disciplineName){
        return repository.findDisciplineLecturerEntityByDisciplineName(disciplineName);
    }
}