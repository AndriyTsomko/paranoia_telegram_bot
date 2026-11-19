package telegram_app.paranoiabottelegram.constants;

import telegram_app.paranoiabottelegram.models.inline_keyboard_buttons.InlineKeyboardCreateAndEditButton;
import telegram_app.paranoiabottelegram.models.inline_keyboard_buttons.InlineKeyboardHomeworkCreatingButton;
import telegram_app.paranoiabottelegram.models.inline_keyboard_buttons.InlineKeyboardSimpleButton;

import java.util.List;

public class Constants {
    public static final String START = "/start";

    public static final String ADD_HOMEWORK = "Додати ДЗ";
    public static final String CHECK_TODAY_HOMEWORK = "Переглянути ДЗ на сьогодні";
    public static final String CHECK_TOMORROW_HOMEWORK = "Переглянути ДЗ на завтра";
    public static final String CHECK_WEEK_HOMEWORK = "Переглянути ДЗ на тиждень";
    public static final String CHECK_HOMEWORK_BY_OWN_TIME = "Переглянути дз за власним періодом часу";
    public static final String ADD_BROADCAST = "Додати оголошення";

    public static final String CALLBACK_ADD_TITLE = "add_title";
    public static final String CALLBACK_ADD_DESCRIPTION = "add_description";
    public static final String CALLBACK_ADD_DEADLINE = "add_deadline";
    public static final String CALLBACK_ADD_FILE = "add_file";
    public static final String CALLBACK_CHOOSE_DISCIPLINE = "choose_discipline";
    public static final String CALLBACK_REVIEW_HOMEWORK = "review_homework";
    public static final String CALLBACK_SAVE_HOMEWORK = "save_homework";
    public static final String CALLBACK_CANCEL = "back";

    public static final String ADD_TITLE = "\uD83D\uDCCC Додати заголовок";
    public static final String ADD_DESCRIPTION = "ℹ\uFE0F Додати опис";
    public static final String ADD_DEADLINE = "\uD83D\uDCC5 *Додати термін виконання";
    public static final String ADD_FILE = "\uD83D\uDCC4 Додати файл";
    public static final String CHOOSE_DISCIPLINE = "\uD83D\uDCDA *Вибрати предмет";
    public static final String REVIEW_HOMEWORK = "\uD83D\uDC40 Переглянути";
    public static final String SAVE_HOMEWORK = "✅ Зберегти";
    public static final String CANCEL = "❌ Скасувати";

    public static final String CALLBACK_EDIT_TITLE = "edit_title";
    public static final String CALLBACK_EDIT_DESCRIPTION = "edit_description";
    public static final String CALLBACK_EDIT_DEADLINE = "edit_deadline";
    public static final String CALLBACK_EDIT_DISCIPLINE = "edit_discipline";

    public static final String EDIT_TITLE = "\uD83D\uDCCC Змінити заголовок";
    public static final String EDIT_DESCRIPTION = "ℹ\uFE0F Змінити опис";
    public static final String EDIT_DEADLINE = "\uD83D\uDCC5 Змінити термін виконання";
    public static final String EDIT_DISCIPLINE = "\uD83D\uDCDA Змінити предмет";

    public static final List<InlineKeyboardHomeworkCreatingButton> INLINE_KEYBOARD_HOMEWORK_CREATING_BUTTON_LIST = List.of(
            InlineKeyboardCreateAndEditButton.builder()
                    .callbackName(CALLBACK_ADD_TITLE)
                    .name(ADD_TITLE)
                    .editCommandName(EDIT_TITLE)
                    .callbackEditCommandName(CALLBACK_EDIT_TITLE)
                    .isFilled(user -> user.getTitle() != null && !user.getTitle().isBlank())
                    .build(),

            InlineKeyboardCreateAndEditButton.builder()
                    .callbackName(CALLBACK_ADD_DESCRIPTION)
                    .name(ADD_DESCRIPTION)
                    .editCommandName(EDIT_DESCRIPTION)
                    .callbackEditCommandName(CALLBACK_EDIT_DESCRIPTION)
                    .isFilled(user -> user.getDescription() != null && !user.getDescription().isBlank())
                    .build(),

            InlineKeyboardCreateAndEditButton.builder()
                    .callbackName(CALLBACK_ADD_DEADLINE)
                    .name(ADD_DEADLINE)
                    .editCommandName(EDIT_DEADLINE)
                    .callbackEditCommandName(CALLBACK_EDIT_DEADLINE)
                    .isFilled(user -> user.getDeadline() != null)
                    .build(),

            InlineKeyboardCreateAndEditButton.builder()
                    .callbackName(CALLBACK_ADD_FILE)
                    .name(ADD_FILE)
                    .build(),

            InlineKeyboardCreateAndEditButton.builder()
                    .callbackName(CALLBACK_CHOOSE_DISCIPLINE)
                    .name(CHOOSE_DISCIPLINE)
                    .editCommandName(EDIT_DISCIPLINE)
                    .callbackEditCommandName(CALLBACK_EDIT_DISCIPLINE)
                    .isFilled(user -> user.getDisciplineLecturer() != null)
                    .build(),

            InlineKeyboardSimpleButton.builder()
                    .callbackName(CALLBACK_REVIEW_HOMEWORK)
                    .name(REVIEW_HOMEWORK)
                    .build(),

            InlineKeyboardSimpleButton.builder()
                    .callbackName(CALLBACK_SAVE_HOMEWORK)
                    .name(SAVE_HOMEWORK)
                    .build(),

            InlineKeyboardSimpleButton.builder()
                    .callbackName(CALLBACK_CANCEL)
                    .name(CANCEL)
                    .build()
    );
}