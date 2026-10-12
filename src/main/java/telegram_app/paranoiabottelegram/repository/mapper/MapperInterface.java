package telegram_app.paranoiabottelegram.repository.mapper;

public interface MapperInterface<Model, Entity> {
    public Entity toEntity(Model model);
    public Model toModel(Entity entity);
}
