package telegram_app.paranoiabottelegram.models.inline_keyboard_buttons;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class InlineKeyboardCreateAndEditButton extends InlineKeyboardHomeworkCreatingButton{
    private final String editCommandName;
    private final String callbackEditCommandName;

    public InlineKeyboardCreateAndEditButton(String callbackName, String name, String editCommandName, String callbackEditCommandName) {
        super(callbackName, name);
        this.editCommandName = editCommandName;
        this.callbackEditCommandName = callbackEditCommandName;
    }
}
