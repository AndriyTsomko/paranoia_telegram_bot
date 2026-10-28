package telegram_app.paranoiabottelegram.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telegram_app.paranoiabottelegram.entity.HomeworkEntity;
import telegram_app.paranoiabottelegram.models.Homework;
import telegram_app.paranoiabottelegram.repository.jpa.JpaHomeworkRepository;
import telegram_app.paranoiabottelegram.repository.mapper.DisciplineLecturerMapper;
import telegram_app.paranoiabottelegram.repository.mapper.HomeworkMapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeworkService
        implements ServiceInterface<Homework, HomeworkEntity, Long>{

    private final HomeworkMapper homeworkMapper;
    private final JpaHomeworkRepository repository;
    private final DisciplineLecturerMapper disciplineLecturerMapper;

    @Override
    public HomeworkEntity save(Homework homework) {
        return repository.save(homeworkMapper.toEntity(homework));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void update(Homework modelToUpdate, Homework newModel) {
        HomeworkEntity entity = repository
                .findById(modelToUpdate.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format(
                                "Домашнє завдання з id %d не було знайдено", modelToUpdate.getId()
                        )
                ));
        entity.setDisciplineLecturer(disciplineLecturerMapper.toEntity(
                modelToUpdate.getDisciplineLecturer()
        ));
        entity.setCreatedTime(newModel.getCreatedTime());
        entity.setTitle(newModel.getTitle());
        entity.setDescription(newModel.getDescription());
        entity.setTelegramId(newModel.getTelegramId());
        entity.setFileId(newModel.getFileId());
        entity.setDeadline(newModel.getDeadline());
        repository.save(entity);
    }

    @Override
    public List<Homework> findAll() {
        return repository.findAll().stream().map(homeworkMapper::toModel).toList();
    }

    @Override
    public Optional<Homework> findById(Long id) {
        return repository.findById(id).stream().map(homeworkMapper::toModel).findFirst();
    }

    public List<Homework> findTodayHomeworks() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return repository.findHomeworkEntitiesByDeadlineBetween(startOfDay, endOfDay)
                .stream()
                .map(homeworkMapper::toModel)
                .toList();
    }
    public List<Homework> findWeekHomeworks() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
        return repository.findHomeworkEntitiesByDeadlineBetween(startOfWeek, endOfWeek)
                .stream()
                .map(homeworkMapper::toModel)
                .toList();
    }

}
