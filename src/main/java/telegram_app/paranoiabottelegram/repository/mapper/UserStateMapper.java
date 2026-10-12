package telegram_app.paranoiabottelegram.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.entity.UserStateEntity;
import telegram_app.paranoiabottelegram.models.UserState;

@Component
@AllArgsConstructor
public class UserStateMapper implements MapperInterface<UserState, UserStateEntity>{

    private final DisciplineLecturerMapper disciplineLecturerMapper;
    @Override
    public UserStateEntity toEntity(UserState userState) {
        if (userState == null) {
            return null;
        }
        return UserStateEntity.builder()
                .chatId(userState.getChatId())
                .state(userState.getState())
                .disciplineLecturer(disciplineLecturerMapper.toEntity(userState.getDisciplineLecturer()))
                .title(userState.getTitle())
                .description(userState.getDescription())
                .fileId(userState.getFileId())
                .deadline(userState.getDeadline())
                .build();
    }

    @Override
    public UserState toModel(UserStateEntity userStateEntity) {
        if (userStateEntity == null) {
            return null;
        }
        return UserState.builder()
                .chatId(userStateEntity.getChatId())
                .state(userStateEntity.getState())
                .disciplineLecturer(disciplineLecturerMapper.toModel(userStateEntity.getDisciplineLecturer()))
                .title(userStateEntity.getTitle())
                .description(userStateEntity.getDescription())
                .fileId(userStateEntity.getFileId())
                .deadline(userStateEntity.getDeadline())
                .build();
    }
}
