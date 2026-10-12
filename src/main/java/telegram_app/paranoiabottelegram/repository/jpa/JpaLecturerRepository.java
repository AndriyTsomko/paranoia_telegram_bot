package telegram_app.paranoiabottelegram.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram_app.paranoiabottelegram.entity.LecturerEntity;

@Repository
public interface JpaLecturerRepository extends JpaRepository<LecturerEntity, Long> {
}
