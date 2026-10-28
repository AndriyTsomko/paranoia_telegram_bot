package telegram_app.paranoiabottelegram.handlers.message_input_handlers;

import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractFieldInputMessageHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class FileFieldInputHandler extends AbstractFieldInputMessageHandler {
    public FileFieldInputHandler(KeyboardService keyboardService,
                                 TelegramMessageSender telegramMessageSender,
                                 UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    protected BotState getTargetState() {
        return BotState.AWAITING_FILE;
    }

    @Override
    protected void applyValue(UserState state, String value) {
        //todo
    }
}
