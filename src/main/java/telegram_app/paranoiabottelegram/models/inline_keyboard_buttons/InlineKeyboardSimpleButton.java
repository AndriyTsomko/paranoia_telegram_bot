package telegram_app.paranoiabottelegram.models.inline_keyboard_buttons;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class InlineKeyboardSimpleButton extends InlineKeyboardHomeworkCreatingButton{
    public InlineKeyboardSimpleButton(String callbackName, String name) {
        super(callbackName, name);
    }
}
