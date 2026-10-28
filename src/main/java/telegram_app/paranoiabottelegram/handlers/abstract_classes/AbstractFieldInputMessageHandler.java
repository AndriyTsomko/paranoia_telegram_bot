package telegram_app.paranoiabottelegram.handlers.abstract_classes;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import telegram_app.paranoiabottelegram.handlers.interfaces.MessageHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

public abstract class AbstractFieldInputMessageHandler implements MessageHandler {
    protected final KeyboardService keyboardService;
    protected final TelegramMessageSender telegramMessageSender;
    protected final UserStateService userStateService;

    public AbstractFieldInputMessageHandler(
            KeyboardService keyboardService,
            TelegramMessageSender telegramMessageSender,
            UserStateService userStateService) {

        this.keyboardService = keyboardService;
        this.telegramMessageSender = telegramMessageSender;
        this.userStateService = userStateService;
    }

    @Override
    public boolean canHandle(Message message, UserState userState) {
        return message.hasText() && userState.getState().equals(getTargetState());
    }

    @Override
    public void handle(Message message, UserState userState) {
        Long chatId = userState.getChatId();

        applyValue(userState, message.getText());
        userState.setState(BotState.CREATING_HOMEWORK);
        userStateService.save(userState);

        telegramMessageSender.sendMessage(chatId, "Оберіть пункт меню", keyboardService.getHomeworkInlineKeyboard());
    }

    protected abstract BotState getTargetState();

    protected abstract void applyValue(UserState state, String value);
}
