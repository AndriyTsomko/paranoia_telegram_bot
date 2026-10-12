package telegram_app.paranoiabottelegram.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.entity.DisciplineLecturerEntity;
import telegram_app.paranoiabottelegram.services.DisciplineLecturerService;

import java.util.List;

@Component
public class KeyboardService {
    public SendMessage getKeyboard(Long chatId){
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Constants.ADD_HOMEWORK));
        row1.add(new KeyboardButton(Constants.CHECK_ALL_HOMEWORK));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Constants.CHECK_TODAY_HOMEWORK));
        row2.add(new KeyboardButton(Constants.CHECK_WEEK_HOMEWORK));

        ReplyKeyboardMarkup build = ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text("Оберіть потрібний пункт меню")
                .replyMarkup(build)
                .build();
    }

    public SendMessage getLecturerKeyboard(Long chatId, DisciplineLecturerService service) {
        KeyboardRow row1 = new KeyboardRow();

        List<DisciplineLecturerEntity> disciplineEntities = service.findAll();
        for (DisciplineLecturerEntity entity : disciplineEntities) {
            row1.add(entity.getDiscipline().getName());
        }

        ReplyKeyboardMarkup build = ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text("Оберіть потрібний пункт меню")
                .replyMarkup(build)
                .build();
    }

}
