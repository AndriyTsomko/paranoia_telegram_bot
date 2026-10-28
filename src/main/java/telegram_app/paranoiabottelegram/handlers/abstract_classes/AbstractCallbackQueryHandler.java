package telegram_app.paranoiabottelegram.handlers.abstract_classes;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.handlers.interfaces.CallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public abstract class AbstractCallbackQueryHandler implements CallbackQueryHandler {
    protected final KeyboardService keyboardService;
    protected final TelegramMessageSender telegramMessageSender;
    protected final UserStateService userStateService;

    public AbstractCallbackQueryHandler(
            KeyboardService keyboardService,
            TelegramMessageSender telegramMessageSender,
            UserStateService userStateService) {
        this.keyboardService = keyboardService;
        this.telegramMessageSender = telegramMessageSender;
        this.userStateService = userStateService;
    }

    protected abstract String getAddCallback();
    protected abstract String getEditCallback();
    protected abstract String getPromptMessage();
    protected abstract BotState getUpdateStateTo();

    @Override
    public boolean canHandle(CallbackQuery callbackQuery, UserState userState) {
        String data = callbackQuery.getData();
        return data.equals(getAddCallback()) || data.equals(getEditCallback());
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        Long chatId = userState.getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        telegramMessageSender.sendMessage(chatId, getPromptMessage());
        userStateService.updateState(userState, getUpdateStateTo());

        telegramMessageSender.deleteMessage(chatId,messageId);
    }
}
