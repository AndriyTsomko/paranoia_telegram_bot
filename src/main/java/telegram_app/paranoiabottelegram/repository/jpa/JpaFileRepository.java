package telegram_app.paranoiabottelegram.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram_app.paranoiabottelegram.entity.FileEntity;

@Repository
public interface JpaFileRepository extends JpaRepository<FileEntity, Long> {
}
