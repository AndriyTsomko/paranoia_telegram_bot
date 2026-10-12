package telegram_app.paranoiabottelegram.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telegram_app.paranoiabottelegram.entity.LecturerEntity;
import telegram_app.paranoiabottelegram.models.Lecturer;
import telegram_app.paranoiabottelegram.repository.jpa.JpaLecturerRepository;
import telegram_app.paranoiabottelegram.repository.mapper.LecturerMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LecturerService implements ServiceInterface<Lecturer, LecturerEntity, Long>{
    private final LecturerMapper mapper;
    private final JpaLecturerRepository repository;

    @Override
    public LecturerEntity save(Lecturer lecturer) {
        return repository.save(mapper.toEntity(lecturer));
    }

    @Override
    public void delete(Lecturer lecturer) {
        repository.delete(mapper.toEntity(lecturer));
    }

    @Override
    public void update(Lecturer modelToUpdate, Lecturer newModel) {
        LecturerEntity entity = repository
                .findById(modelToUpdate.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format(
                              "Викладача з id %d не було знайдено", modelToUpdate.getId()
                        )
                ));
        entity.setName(newModel.getName());
        repository.save(entity);
    }

    @Override
    public List<LecturerEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<LecturerEntity> findById(Long id) {
        return repository.findById(id);
    }
}
