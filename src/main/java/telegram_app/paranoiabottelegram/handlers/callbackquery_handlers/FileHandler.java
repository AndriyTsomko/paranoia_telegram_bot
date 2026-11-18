    package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class FileHandler extends AbstractCallbackQueryHandler {
    public FileHandler(KeyboardService keyboardService,
                       TelegramMessageSender telegramMessageSender,
                       UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_ADD_FILE;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_EDIT_FILE;
    }

    @Override
    protected String getPromptMessage() {
        return "<b>Надішліть файл чи фото:</b>\n\n<i>*Фото і документи відсилати\n віддільно один від одного</i>";
    }

    @Override
    protected BotState getUpdateStateTo() {
        return BotState.AWAITING_FILE;
    }
}
