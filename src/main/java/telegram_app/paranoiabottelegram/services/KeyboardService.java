package telegram_app.paranoiabottelegram.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.models.inline_keyboard_buttons.InlineKeyboardCreateAndEditButton;
import telegram_app.paranoiabottelegram.models.inline_keyboard_buttons.InlineKeyboardSimpleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static telegram_app.paranoiabottelegram.constants.Constants.ADD_BROADCAST;
import static telegram_app.paranoiabottelegram.constants.Constants.ADD_HOMEWORK;
import static telegram_app.paranoiabottelegram.constants.Constants.CHECK_HOMEWORK_BY_OWN_TIME;
import static telegram_app.paranoiabottelegram.constants.Constants.CHECK_TOMORROW_HOMEWORK;
import static telegram_app.paranoiabottelegram.constants.Constants.CLEAR_FILES;
import static telegram_app.paranoiabottelegram.constants.Constants.INLINE_KEYBOARD_CONFIRMING_OPERATION_BUTTON_LIST;
import static telegram_app.paranoiabottelegram.constants.Constants.INLINE_KEYBOARD_HOMEWORK_CREATING_BUTTON_LIST;
import static telegram_app.paranoiabottelegram.constants.Constants.INLINE_KEYBOARD_REVIEW_HOMEWORK_BUTTON_LIST;
import static telegram_app.paranoiabottelegram.constants.Constants.REVIEW_FILES;

@Component
@RequiredArgsConstructor
public class KeyboardService {
    private final DisciplineLecturerService disciplineLecturerService;
    private final HomeworkService homeworkService;

    public InlineKeyboardMarkup getHomeworkInlineKeyboard(UserState user) {
        List<InlineKeyboardButton> buttons = INLINE_KEYBOARD_HOMEWORK_CREATING_BUTTON_LIST.stream()
                .filter(button -> !(button.getName().equals(CLEAR_FILES) && user.getFiles().isEmpty()))
                .map(button -> {
                            String text;
                            String callbackData;
                            if (button instanceof InlineKeyboardCreateAndEditButton && ((InlineKeyboardCreateAndEditButton) button).isFilled(user)) {
                                text = ((InlineKeyboardCreateAndEditButton) button).getEditCommandName();
                                callbackData = ((InlineKeyboardCreateAndEditButton) button).getCallbackEditCommandName();
                            } else {
                                text = button.getName();
                                callbackData = button.getCallbackName();
                            }
                            return InlineKeyboardButton.builder()
                                    .text(text)
                                    .callbackData(callbackData)
                                    .build();
                        }
                ).collect(Collectors.toList());


        List<InlineKeyboardRow> keyboardRows = new ArrayList<>();
        fillInlineKeyboardRows(keyboardRows, buttons);

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
    }

    public InlineKeyboardMarkup getDisciplinesInlineKeyboard() {
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
        row1.add(new KeyboardButton(ADD_BROADCAST));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Constants.CHECK_TODAY_HOMEWORK));
        row2.add(new KeyboardButton(CHECK_TOMORROW_HOMEWORK));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(Constants.CHECK_WEEK_HOMEWORK));
        row3.add(new KeyboardButton(CHECK_HOMEWORK_BY_OWN_TIME));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2, row3))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public InlineKeyboardMarkup getReviewKeyboard(Long homeworkId) {
        ArrayList<InlineKeyboardSimpleButton> buttons = new ArrayList<>(INLINE_KEYBOARD_REVIEW_HOMEWORK_BUTTON_LIST);
        boolean isEmptyFiles = homeworkService.findById(homeworkId).get().getFiles().isEmpty();

        buttons.removeIf(button -> button.getName().equals(REVIEW_FILES) && isEmptyFiles);
        return getInlineKeyboardByButtonList(buttons, homeworkId);
    }

    public InlineKeyboardMarkup getConfirmOperationKeyboard(Long homeworkId) {
        return getInlineKeyboardByButtonList(new ArrayList<>(INLINE_KEYBOARD_CONFIRMING_OPERATION_BUTTON_LIST), homeworkId);
    }

    private InlineKeyboardMarkup getInlineKeyboardByButtonList(List<InlineKeyboardSimpleButton> buttons, Long homeworkId) {
        List<InlineKeyboardRow> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> collect = buttons.stream()
                .map(button -> InlineKeyboardButton.builder()
                        .text(button.getName())
                        .callbackData(button.getCallbackName() + ":" + homeworkId)
                        .build()
                ).collect(Collectors.toList());

        fillInlineKeyboardRows(keyboardRows, collect);

        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
    }


    private void fillInlineKeyboardRows(List<InlineKeyboardRow> keyboardRows,
                                        List<InlineKeyboardButton> buttons) {
        for (int i = 0; i < buttons.size(); i += 2) {
            if (i + 1 < buttons.size()) {
                keyboardRows.add(new InlineKeyboardRow(buttons.get(i), buttons.get(i + 1)));
            } else {
                keyboardRows.add(new InlineKeyboardRow(buttons.get(i)));
            }
        }
    }

}
