package telegram_app.paranoiabottelegram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_file_id", length = 255,  nullable = false)
    private String telegramFileId;

    @Column(name = "file_extension", length = 45, nullable = false)
    private String fileExtension;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "homework_id", referencedColumnName = "id")
    private HomeworkEntity homework;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userstate_id", referencedColumnName = "chat_id")
    private UserStateEntity userState;
}
