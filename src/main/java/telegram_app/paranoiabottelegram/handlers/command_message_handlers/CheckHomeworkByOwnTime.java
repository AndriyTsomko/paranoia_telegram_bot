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
public class CheckHomeworkByOwnTime extends AbstractCommandMessageHandler {
    public CheckHomeworkByOwnTime(KeyboardService keyboardService,
                                    TelegramMessageSender telegramMessageSender,
                                    UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    protected String getTargetCommand() {
        return Constants.CHECK_HOMEWORK_BY_OWN_TIME;
    }

    @Override
    public void handle(Message message, UserState userState) {
        Long chatId = userState.getChatId();
        telegramMessageSender.sendMessage(chatId, "<b>Введіть дату в форматі 'дата - дата': </b>");
        userStateService.updateState(userState, BotState.AWAITING_DEADLINE_BY_TWO_DATES);
    }
}
