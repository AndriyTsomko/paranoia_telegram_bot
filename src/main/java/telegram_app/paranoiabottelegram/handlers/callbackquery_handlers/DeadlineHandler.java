package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class DeadlineHandler extends AbstractCallbackQueryHandler {
    public DeadlineHandler(KeyboardService keyboardService,
                           TelegramMessageSender telegramMessageSender,
                           UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_ADD_DEADLINE;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_ADD_DEADLINE;
    }

    @Override
    protected String getPromptMessage() {
        return "Введіть термін здачі"; //todo доробити опис форматів
    }

    @Override
    protected BotState getUpdateStateTo() {
        return BotState.AWAITING_DEADLINE;
    }
}
