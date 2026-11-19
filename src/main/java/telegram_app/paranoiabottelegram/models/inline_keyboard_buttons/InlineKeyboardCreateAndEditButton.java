package telegram_app.paranoiabottelegram.models.inline_keyboard_buttons;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import telegram_app.paranoiabottelegram.models.UserState;

import java.util.function.Predicate;

@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class InlineKeyboardCreateAndEditButton extends InlineKeyboardHomeworkCreatingButton{
    private final String editCommandName;
    private final String callbackEditCommandName;
    private Predicate<UserState> isFilled;

    public InlineKeyboardCreateAndEditButton(String callbackName, String name, String editCommandName, String callbackEditCommandName) {
        super(callbackName, name);
        this.editCommandName = editCommandName;
        this.callbackEditCommandName = callbackEditCommandName;
    }

    public boolean isFilled(UserState user){
        return isFilled != null && isFilled.test(user);
    }
}
