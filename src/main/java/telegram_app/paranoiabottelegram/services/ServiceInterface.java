package telegram_app.paranoiabottelegram.services;

import java.util.List;
import java.util.Optional;

public interface ServiceInterface<Model, Entity, Long> {
    Entity save(Model model);
    void delete(Model model);
    void update(Model modelToUpdate, Model newModel);
    List<Entity> findAll();
    Optional<Entity> findById(Long id);
}
