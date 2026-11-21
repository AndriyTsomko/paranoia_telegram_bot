package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class DeleteHomeworkHandler extends AbstractCallbackQueryHandler {
    public DeleteHomeworkHandler(KeyboardService keyboardService,
                                 TelegramMessageSender telegramMessageSender,
                                 UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = userState.getChatId();

        String callbackQueryData = callbackQuery.getData();
        int index = callbackQueryData.indexOf(':');
        Long homeworkId = Long.parseLong(callbackQueryData.substring(index + 1));

        telegramMessageSender.deleteMessage(chatId, messageId);
        telegramMessageSender.sendMessage(chatId, getPromptMessage(),
                keyboardService.getConfirmOperationKeyboard(homeworkId));
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_DELETE_HOMEWORK;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_DELETE_HOMEWORK;
    }

    @Override
    protected String getPromptMessage() {
        return "Підтвердити видалення ДЗ";
    }

    @Override
    protected BotState getUpdateStateTo() {
        return BotState.IDLE;
    }

    @Override
    public boolean canHandle(CallbackQuery callbackQuery, UserState userState) {
        String data = callbackQuery.getData();
        return data.startsWith(getAddCallback()) || data.startsWith(getEditCallback());
    }
}
