package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.DisciplineLecturer;
import telegram_app.paranoiabottelegram.models.Homework;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.HomeworkService;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

import java.time.LocalDateTime;

@Component
public class SaveHandler extends AbstractCallbackQueryHandler {
    private final HomeworkService homeworkService;

    public SaveHandler(KeyboardService keyboardService,
                       TelegramMessageSender telegramMessageSender,
                       UserStateService userStateService, HomeworkService homeworkService) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.homeworkService = homeworkService;
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        Long chatId = userState.getChatId();
        DisciplineLecturer disciplineLecturer = userState.getDisciplineLecturer();
        LocalDateTime deadline = userState.getDeadline();

        if (disciplineLecturer == null){
            telegramMessageSender.sendMessage(chatId, "Щоб зберегти ДЗ потрібно вибрати предмет");
        } else if (deadline == null) {
            telegramMessageSender.sendMessage(chatId, "Щоб зберегти ДЗ потрібно додати дедлайн");
        } else {
            Integer messageId = callbackQuery.getMessage().getMessageId();
            telegramMessageSender.deleteMessage(chatId, messageId);

            Homework homework = Homework.builder()
                    .createdTime(LocalDateTime.now())
                    .disciplineLecturer(disciplineLecturer)
                    .title(userState.getTitle())
                    .description(userState.getDescription())
                    .telegramId(chatId)
                    .fileId(userState.getFileId())
                    .deadline(deadline)
                    .build();
            homeworkService.save(homework);
            telegramMessageSender.sendMessage(chatId, "ДЗ успішно збережено");

            userState.clear();
            userStateService.save(userState);
        }
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_SAVE_HOMEWORK;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_SAVE_HOMEWORK;
    }

    @Override
    protected String getPromptMessage() {
        return null;
    }

    @Override
    protected BotState getUpdateStateTo() {
        return BotState.IDLE;
    }
}
