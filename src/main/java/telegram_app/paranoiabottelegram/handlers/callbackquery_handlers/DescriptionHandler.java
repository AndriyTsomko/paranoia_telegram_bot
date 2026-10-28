package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class DescriptionHandler extends AbstractCallbackQueryHandler {

    public DescriptionHandler(KeyboardService keyboardService,
                              TelegramMessageSender telegramMessageSender,
                              UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_ADD_DESCRIPTION;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_EDIT_DESCRIPTION;
    }

    @Override
    protected String getPromptMessage() {
        return "Введіть опис";
    }

    @Override
    protected BotState getUpdateStateTo() {
        return BotState.AWAITING_DESCRIPTION;
    }
}
