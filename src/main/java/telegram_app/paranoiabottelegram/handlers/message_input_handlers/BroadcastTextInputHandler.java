package telegram_app.paranoiabottelegram.handlers.message_input_handlers;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractFieldInputMessageHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class BroadcastTextInputHandler extends AbstractFieldInputMessageHandler {
    private final TelegramClient telegramClient;

    public BroadcastTextInputHandler(KeyboardService keyboardService,
                                     TelegramMessageSender telegramMessageSender,
                                     UserStateService userStateService, TelegramClient telegramClient) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.telegramClient = telegramClient;
    }

    @Override
    protected BotState getTargetState() {
        return BotState.AWAITING_BROADCAST_TEXT;
    }

    @Override
    protected void applyValue(UserState state, String value) {
    }

    @SneakyThrows
    @Override
    public void handle(Message message, UserState userState) {
        Long chatId = userState.getChatId();
        String senderUserName = telegramClient.execute(new GetChat(chatId.toString())).getUserName();
        String text = message.getText();

        String broadcastMessageTextFormat = String.format(
                "<b>\uD83D\uDCACПовідомлення від @%s</b>\n\n%s",
                senderUserName, text
        );
        userStateService.findAll().forEach(
                user -> {
                    if (!user.getChatId().equals(chatId)){
                        telegramMessageSender.sendMessage(user.getChatId(), broadcastMessageTextFormat);
                    }
                }
        );
        telegramMessageSender.deleteMessage(chatId, message.getMessageId());
        telegramMessageSender.sendMessage(chatId, "✅Повідомлення успішно надіслане");

        userStateService.updateState(userState, BotState.IDLE);
    }
}
