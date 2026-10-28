package telegram_app.paranoiabottelegram.handlers.abstract_classes;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import telegram_app.paranoiabottelegram.handlers.interfaces.MessageHandler;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

public abstract class AbstractCommandMessageHandler implements MessageHandler {
    protected final KeyboardService keyboardService;
    protected final TelegramMessageSender telegramMessageSender;
    protected final UserStateService userStateService;

    public AbstractCommandMessageHandler(KeyboardService keyboardService, TelegramMessageSender telegramMessageSender, UserStateService userStateService) {
        this.keyboardService = keyboardService;
        this.telegramMessageSender = telegramMessageSender;
        this.userStateService = userStateService;
    }

    protected abstract String getTargetCommand();

    @Override
    public boolean canHandle(Message message, UserState userState) {
        return message.hasText() && message.getText().equals(getTargetCommand());
    }
}
