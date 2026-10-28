package telegram_app.paranoiabottelegram.handlers.interfaces;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.models.UserState;

public interface CallbackQueryHandler{
    boolean canHandle(CallbackQuery callbackQuery, UserState userState);
    void handle(CallbackQuery callbackQuery, UserState userState);
}
