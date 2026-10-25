package telegram_app.paranoiabottelegram.models.inline_keyboard_buttons;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder
public abstract class InlineKeyboardHomeworkCreatingButton {
    final String callbackName;
    final String name;
}
