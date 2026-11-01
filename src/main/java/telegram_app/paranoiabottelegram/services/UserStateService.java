package telegram_app.paranoiabottelegram.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import telegram_app.paranoiabottelegram.entity.UserStateEntity;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.repository.jpa.JpaUserStateRepository;
import telegram_app.paranoiabottelegram.repository.mapper.DisciplineLecturerMapper;
import telegram_app.paranoiabottelegram.repository.mapper.FileMapper;
import telegram_app.paranoiabottelegram.repository.mapper.UserStateMapper;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserStateService
        implements ServiceInterface<UserState, UserStateEntity, Long> {
    private final JpaUserStateRepository repository;

    private final UserStateMapper userStateMapper;
    private final DisciplineLecturerMapper disciplineLecturerMapper;
    private final FileMapper fileMapper;

    public UserState getOrCreate(Long telegramId){
        return repository.findById(telegramId)
                .map(userStateMapper::toModel)
                .orElseGet(
                        () -> {
                            UserState newUser = UserState.builder()
                                    .chatId(telegramId)
                                    .state(BotState.IDLE)
                                    .build();
                            repository.save(userStateMapper.toEntity(newUser));
                            return newUser;
                        });
    }

    public void updateState(UserState user, BotState state) {
        UserStateEntity userStateEntity = repository.findById(user.getChatId())
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format(
                                        "UserState з id %d не було знайдено", user.getChatId()
                                )
                        )
                );
        userStateEntity.setState(state);
        repository.save(userStateEntity);
    }

    @Override
    public UserStateEntity save(UserState userState) {
        return repository.save(userStateMapper.toEntity(userState));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void update(UserState modelToUpdate, UserState newModel) {
        UserStateEntity userStateEntity = repository.findById(modelToUpdate.getChatId())
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format(
                                        "UserState з id %d не було знайдено", modelToUpdate.getChatId()
                                )
                        )
                );
        userStateEntity.setChatId(newModel.getChatId());
        userStateEntity.setState(newModel.getState());
        userStateEntity.setDisciplineLecturer(disciplineLecturerMapper.toEntity(newModel.getDisciplineLecturer()));
        userStateEntity.setTitle(newModel.getTitle());
        userStateEntity.setDescription(newModel.getDescription());
        userStateEntity.setFiles(newModel.getFiles().stream().map(fileMapper::toEntity).toList());
        userStateEntity.setDeadline(newModel.getDeadline());
        repository.save(userStateEntity);
    }

    @Override
    public List<UserState> findAll() {
        return repository.findAll().stream().map(userStateMapper::toModel).toList();
    }

    @Override
    public Optional<UserState> findById(Long id) {
        return repository.findById(id).stream().map(userStateMapper::toModel).findFirst();
    }
}
