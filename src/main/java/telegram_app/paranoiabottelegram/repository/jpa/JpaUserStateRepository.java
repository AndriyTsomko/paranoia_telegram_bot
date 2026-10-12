package telegram_app.paranoiabottelegram.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram_app.paranoiabottelegram.entity.UserStateEntity;

import java.util.Optional;

@Repository
public interface JpaUserStateRepository extends JpaRepository<UserStateEntity, Long> {
}
