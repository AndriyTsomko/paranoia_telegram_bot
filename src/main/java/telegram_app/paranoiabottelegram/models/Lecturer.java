package telegram_app.paranoiabottelegram.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Lecturer {
    private Long id;
    private String name;
}
