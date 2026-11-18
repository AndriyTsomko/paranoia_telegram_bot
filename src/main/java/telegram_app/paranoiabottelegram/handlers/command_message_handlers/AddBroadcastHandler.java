package telegram_app.paranoiabottelegram.handlers.command_message_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCommandMessageHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class AddBroadcastHandler extends AbstractCommandMessageHandler {
    public AddBroadcastHandler(KeyboardService keyboardService,
                               TelegramMessageSender telegramMessageSender,
                               UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    protected String getTargetCommand() {
        return Constants.ADD_BROADCAST;
    }

    @Override
    public void handle(Message message, UserState userState) {
        telegramMessageSender.sendMessage(userState.getChatId(), "Введіть текст оголошення: ");
        userStateService.updateState(userState, BotState.AWAITING_BROADCAST_TEXT);
    }
}
