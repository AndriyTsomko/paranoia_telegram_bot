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
public class CancelHandler extends AbstractCallbackQueryHandler {
    public CancelHandler(KeyboardService keyboardService,
                         TelegramMessageSender telegramMessageSender,
                         UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        Long chatId = userState.getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        telegramMessageSender.deleteMessage(chatId, messageId);
        userState.clear();
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_CANCEL;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_CANCEL;
    }

    @Override
    protected String getPromptMessage() {
        return null;
    }

    @Override
    protected BotState getUpdateStateTo() {
        return null;
    }
}
