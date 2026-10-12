package telegram_app.paranoiabottelegram.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import telegram_app.paranoiabottelegram.entity.UserStateEntity;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.repository.jpa.JpaUserStateRepository;
import telegram_app.paranoiabottelegram.repository.mapper.DisciplineLecturerMapper;
import telegram_app.paranoiabottelegram.repository.mapper.UserStateMapper;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserStateService implements ServiceInterface<UserState, UserStateEntity, Long> {
    private final JpaUserStateRepository repository;

    private final UserStateMapper mapper;
    private final DisciplineLecturerMapper disciplineLecturerMapper;

    public UserState getOrCreate(Long telegramId){
        return repository.findById(telegramId)
                .map(mapper::toModel)
                .orElseGet(
                        () -> {
                            UserState newUser = UserState.builder()
                                    .chatId(telegramId)
                                    .state(BotState.IDLE)
                                    .build();
                            repository.save(mapper.toEntity(newUser));
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
        return repository.save(mapper.toEntity(userState));
    }

    @Override
    public void delete(UserState userState) {
        repository.delete(mapper.toEntity(userState));
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
        userStateEntity = UserStateEntity.builder()
                .chatId(newModel.getChatId())
                .state(newModel.getState())
                .disciplineLecturer(disciplineLecturerMapper.toEntity(newModel.getDisciplineLecturer()))
                .title(newModel.getTitle())
                .description(newModel.getDescription())
                .fileId(newModel.getFileId())
                .deadline(newModel.getDeadline())
                .build();
        repository.save(userStateEntity);
    }

    @Override
    public List<UserStateEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserStateEntity> findById(Long id) {
        return repository.findById(id);
    }
}
