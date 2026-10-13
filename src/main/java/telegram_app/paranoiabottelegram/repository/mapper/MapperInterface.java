package telegram_app.paranoiabottelegram.repository.mapper;

public interface MapperInterface<Model, Entity> {
    Entity toEntity(Model model);

    Model toModel(Entity entity);
}
