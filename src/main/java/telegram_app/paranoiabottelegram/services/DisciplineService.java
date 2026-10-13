package telegram_app.paranoiabottelegram.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telegram_app.paranoiabottelegram.entity.DisciplineEntity;
import telegram_app.paranoiabottelegram.models.Discipline;
import telegram_app.paranoiabottelegram.repository.jpa.JpaDisciplineRepository;
import telegram_app.paranoiabottelegram.repository.mapper.DisciplineMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisciplineService implements ServiceInterface<Discipline, DisciplineEntity, Long>{
    private final DisciplineMapper mapper;
    private final JpaDisciplineRepository repository;

    @Override
    public DisciplineEntity save(Discipline discipline) {
        return repository.save(mapper.toEntity(discipline));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void update(Discipline modelToUpdate, Discipline newModel) {
        DisciplineEntity entity = repository
                .findById(modelToUpdate.getId())
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format(
                                        "Дисципліну з id %d не була знайдена", modelToUpdate.getId()
                                )
                        )
                );
        entity.setName(newModel.getName());
        repository.save(entity);
    }

    @Override
    public List<Discipline> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<Discipline> findById(Long id) {
        return repository.findById(id).stream().map(mapper::toModel).findFirst();
    }
}
