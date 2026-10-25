package telegram_app.paranoiabottelegram.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.services.DisciplineLecturerService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static telegram_app.paranoiabottelegram.constants.Constants.*;

@Component
public class KeyboardService {
    public InlineKeyboardMarkup getHomeworkInlineKeyboard() {
        List<InlineKeyboardButton> buttons = INLINE_KEYBOARD_HOMEWORK_CREATING_BUTTON_LIST.stream()
                .map(button -> InlineKeyboardButton.builder()
                        .text(button.getName())
                        .callbackData(button.getCallbackName())
                        .build())
                .collect(Collectors.toList());


        List<InlineKeyboardRow> keyboardRows = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i += 2) {
            if (i + 1 < buttons.size()) {
                keyboardRows.add(new InlineKeyboardRow(buttons.get(i), buttons.get(i + 1)));
            } else {
                keyboardRows.add(new InlineKeyboardRow(buttons.get(i)));
            }
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
    }

    public InlineKeyboardMarkup getDisciplinesInlineKeyboard
            (DisciplineLecturerService disciplineLecturerService) {

        return InlineKeyboardMarkup.builder()
                .keyboard(disciplineLecturerService.findAll().stream()
                        .map(
                                model -> new InlineKeyboardRow(InlineKeyboardButton.builder()
                                        .text(model.getDiscipline().getName())
                                        .callbackData(model.getDiscipline().getName())
                                        .build())
                        )
                        .toList())
                .build();

    }

    public ReplyKeyboardMarkup getMainKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ADD_HOMEWORK));
        row1.add(new KeyboardButton(CHECK_HOMEWORK_BY_OWN_TIME));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Constants.CHECK_TODAY_HOMEWORK));
        row2.add(new KeyboardButton(Constants.CHECK_WEEK_HOMEWORK));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardRemove replyKeyboardRemove() {
        return ReplyKeyboardRemove.builder()
                .removeKeyboard(true)
                .build();
    }

}
