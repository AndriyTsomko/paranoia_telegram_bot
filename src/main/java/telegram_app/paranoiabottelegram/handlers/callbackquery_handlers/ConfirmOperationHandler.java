package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.HomeworkService;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class ConfirmOperationHandler extends AbstractCallbackQueryHandler {
    private final HomeworkService homeworkService;

    public ConfirmOperationHandler(KeyboardService keyboardService,
                                   TelegramMessageSender telegramMessageSender,
                                   UserStateService userStateService, HomeworkService homeworkService) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.homeworkService = homeworkService;
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_CONFIRM_OPERATION;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_CONFIRM_OPERATION;
    }

    @Override
    protected String getPromptMessage() {
        return "Дз успішно видалено";
    }

    @Override
    protected BotState getUpdateStateTo() {
        return BotState.IDLE;
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = userState.getChatId();

        String callbackQueryData = callbackQuery.getData();
        int index = callbackQueryData.indexOf(':');
        Long homeworkId = Long.parseLong(callbackQueryData.substring(index + 1));

        if (!chatId.equals(userState.getChatId())) {
            telegramMessageSender.sendMessage(chatId, "У вас нема прав на видалення цього завдання");
            return;
        }

        homeworkService.delete(homeworkId);
        telegramMessageSender.deleteMessage(chatId, messageId);
        telegramMessageSender.sendMessage(chatId, getPromptMessage());
    }

    @Override
    public boolean canHandle(CallbackQuery callbackQuery, UserState userState) {
        String data = callbackQuery.getData();
        return data.startsWith(getAddCallback()) || data.startsWith(getEditCallback());
    }
}
