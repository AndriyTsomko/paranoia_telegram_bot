package telegram_app.paranoiabottelegram.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class File {
    private Long id;
    private String telegramFileId;
    private String fileExtension;
    private Homework homework;
    private UserState userState;
}
