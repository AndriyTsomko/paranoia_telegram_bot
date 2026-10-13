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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.Date;

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

    public List<Homework> findTodayHomeworks(){
        return repository.findHomeworkEntitiesByDeadlineBetween(
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.now().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()))
                .stream().map(homeworkMapper::toModel).toList();
    }

    public List<Homework> findWeekHomeworks(){
        LocalDate today = LocalDate.now();
        ZoneId systemZone = ZoneId.systemDefault();

        Date startOfWeek = Date.from(
                today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                        .atStartOfDay(systemZone)
                        .toInstant()
        );
        Date endOfWeek = Date.from(
                today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                        .atTime(LocalTime.MAX)
                        .atZone(systemZone)
                        .toInstant()
        );

        return repository.findHomeworkEntitiesByDeadlineBetween(startOfWeek, endOfWeek).stream()
                .map(homeworkMapper::toModel)
                .toList();
    }

}
