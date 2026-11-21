package telegram_app.paranoiabottelegram.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import telegram_app.paranoiabottelegram.entity.FileEntity;
import telegram_app.paranoiabottelegram.models.File;
import telegram_app.paranoiabottelegram.repository.jpa.JpaFileRepository;
import telegram_app.paranoiabottelegram.repository.mapper.FileMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService implements ServiceInterface<File, FileEntity, Long>{
    private final FileMapper mapper;
    private final JpaFileRepository repository;

    @Override
    public FileEntity save(File file) {
        return repository.save(mapper.toEntity(file));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void update(File modelToUpdate, File newModel) {
        FileEntity fileEntity = repository.findById(modelToUpdate.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Файл з id %d не було знайдено", modelToUpdate.getId())
                ));
        fileEntity.setTelegramFileId(newModel.getTelegramFileId());
        fileEntity.setFileExtension(newModel.getFileExtension());
        repository.save(fileEntity);
    }

    @Override
    public List<File> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<File> findById(Long id) {
        return repository.findById(id).stream().map(mapper::toModel).findFirst();
    }

    public List<File> findFilesByUserStateChatId(Long chatId) {
        return repository.findFileEntitiesByUserState_ChatId(chatId).stream().map(mapper::toModel).toList();
    }

    public List<File> findFilesByHomeworkId(Long homeworkId) {
        return repository.findFileEntitiesByHomeworkId(homeworkId).stream().map(mapper::toModel).toList();
    }
}
