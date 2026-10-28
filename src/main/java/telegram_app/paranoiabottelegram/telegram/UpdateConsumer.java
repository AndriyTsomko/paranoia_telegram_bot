package telegram_app.paranoiabottelegram.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import telegram_app.paranoiabottelegram.handlers.interfaces.CallbackQueryHandler;
import telegram_app.paranoiabottelegram.handlers.interfaces.MessageHandler;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.UserStateService;

import java.util.List;


@Component
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final List<CallbackQueryHandler> callbackQueryHandlerList;
    private final List<MessageHandler> messageHandlerList;
    private final UserStateService userStateService;

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            UserState user = userStateService.getOrCreate(update.getMessage().getChatId());

            Message message = update.getMessage();

            messageHandlerList.stream()
                    .filter(messageHandler -> messageHandler.canHandle(message, user))
                    .findFirst()
                    .ifPresent(handler -> handler.handle(message, user));
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            UserState user = userStateService.getOrCreate(callbackQuery.getMessage().getChatId());

            callbackQueryHandlerList.stream()
                    .filter(handler -> handler.canHandle(callbackQuery, user))
                    .findFirst()
                    .ifPresent(handler -> handler.handle(callbackQuery, user));
        }

    }
}
