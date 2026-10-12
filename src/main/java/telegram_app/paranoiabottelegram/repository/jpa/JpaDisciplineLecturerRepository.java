package telegram_app.paranoiabottelegram.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegram_app.paranoiabottelegram.entity.DisciplineLecturerEntity;

import java.util.Optional;

@Repository
public interface JpaDisciplineLecturerRepository extends JpaRepository<DisciplineLecturerEntity, Long> {
    Optional<DisciplineLecturerEntity> findDisciplineLecturerEntityByDisciplineName(String disciplineName);
}
