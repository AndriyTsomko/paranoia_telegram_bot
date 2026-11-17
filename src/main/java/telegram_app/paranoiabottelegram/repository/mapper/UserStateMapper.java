package telegram_app.paranoiabottelegram.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.entity.FileEntity;
import telegram_app.paranoiabottelegram.entity.UserStateEntity;
import telegram_app.paranoiabottelegram.models.UserState;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserStateMapper implements MapperInterface<UserState, UserStateEntity>{
    private final DisciplineLecturerMapper disciplineLecturerMapper;
    private final FileMapper fileMapper;
    @Override
    public UserStateEntity toEntity(UserState userState) {
        if (userState == null) {
            return null;
        }
        UserStateEntity userStateEntity = UserStateEntity.builder()
                .chatId(userState.getChatId())
                .state(userState.getState())
                .disciplineLecturer(disciplineLecturerMapper.toEntity(userState.getDisciplineLecturer()))
                .title(userState.getTitle())
                .description(userState.getDescription())
                .deadline(userState.getDeadline())
                .build();

        if (userState.getFiles() != null) {
            List<FileEntity> fileEntities = userState.getFiles().stream()
                    .map(fileMapper::toEntity)
                    .peek(fileEntity -> fileEntity.setUserState(userStateEntity))
                    .toList();
            userStateEntity.setFiles(fileEntities);
        }

        return userStateEntity;
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
                .files(userStateEntity.getFiles() != null
                        ? new ArrayList<>(userStateEntity.getFiles().stream().map(fileMapper::toModel).toList())
                        : new ArrayList<>())
                .deadline(userStateEntity.getDeadline())
                .build();
    }
}
