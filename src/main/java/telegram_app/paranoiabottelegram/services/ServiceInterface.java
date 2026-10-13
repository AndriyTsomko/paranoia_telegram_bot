package telegram_app.paranoiabottelegram.services;

import java.util.List;
import java.util.Optional;

public interface ServiceInterface<Model, Entity, ID> {
    Entity save(Model model);
    void delete(ID id);
    void update(Model modelToUpdate, Model newModel);
    List<Model> findAll();
    Optional<Model> findById(ID id);
}
