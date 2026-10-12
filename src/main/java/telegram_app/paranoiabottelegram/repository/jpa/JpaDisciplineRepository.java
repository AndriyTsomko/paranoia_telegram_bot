package telegram_app.paranoiabottelegram.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram_app.paranoiabottelegram.entity.DisciplineEntity;

@Repository
public interface JpaDisciplineRepository extends JpaRepository<DisciplineEntity, Long> {
}
