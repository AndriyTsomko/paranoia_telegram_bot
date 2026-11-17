package telegram_app.paranoiabottelegram.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram_app.paranoiabottelegram.entity.FileEntity;

import java.util.List;

@Repository
public interface JpaFileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findFileEntitiesByUserState_ChatId(Long userStateChatId);
}
