package telegram_app.paranoiabottelegram.handlers.command_message_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCommandMessageHandler;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class StartHandler extends AbstractCommandMessageHandler {
    public StartHandler(KeyboardService keyboardService,
                        TelegramMessageSender telegramMessageSender,
                        UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    public void handle(Message message, UserState userState) {
        Long chatId = userState.getChatId();
        String text = "Виберіть пункт меню";
        telegramMessageSender.sendMessage(chatId,text,keyboardService.getMainKeyboard());

        userState.clear();
        userStateService.save(userState);
    }

    @Override
    protected String getTargetCommand() {
        return Constants.START;
    }
}
