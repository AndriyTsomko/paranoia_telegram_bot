package telegram_app.paranoiabottelegram.repository.mapper;

import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.entity.FileEntity;
import telegram_app.paranoiabottelegram.models.File;

@Component
public class FileMapper implements MapperInterface<File, FileEntity> {
    @Override
    public FileEntity toEntity(File file) {
        if (file == null) {
            return  null;
        }
        return FileEntity.builder()
                .id(file.getId())
                .telegramFileId(file.getTelegramFileId())
                .fileExtension(file.getFileExtension())
                .build();
    }

    @Override
    public File toModel(FileEntity fileEntity) {
        if (fileEntity == null) {
            return  null;
        }
        return File.builder()
                .id(fileEntity.getId())
                .telegramFileId(fileEntity.getTelegramFileId())
                .fileExtension(fileEntity.getFileExtension())
                .build();
    }
}
