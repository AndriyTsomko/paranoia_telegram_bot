package telegram_app.paranoiabottelegram.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram_app.paranoiabottelegram.entity.HomeworkEntity;

import java.util.Date;
import java.util.List;

@Repository
public interface JpaHomeworkRepository extends JpaRepository<HomeworkEntity, Long> {
    List<HomeworkEntity> findHomeworkEntitiesByDeadlineBetween(Date deadlineAfter, Date deadlineBefore);
}
