package telegram_app.paranoiabottelegram.handlers.interfaces;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import telegram_app.paranoiabottelegram.models.UserState;

public interface MessageHandler {
    boolean canHandle(Message message, UserState userState);
    void handle(Message message, UserState userState);
}
