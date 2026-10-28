package telegram_app.paranoiabottelegram.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class TelegramMessageSender {
    private final TelegramClient telegramClient;

    public void sendMessage(Long chatId, String text){
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        processingSendMessage(sendMessage, chatId, text);
    }

    public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard){
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .build();
        processingSendMessage(sendMessage, chatId, text);
    }

    public void deleteMessage(Long chatId, Integer messageId) {
        DeleteMessage deleteUserInputMessage = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
        processingDeleteMessage(deleteUserInputMessage, chatId);
    }

    private void processingSendMessage(SendMessage sendMessage, Long chatId, String text){
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(String.format(
                    "Помилка при відправці повідомлення\n\n\"%s\"\n\n до користувача з id %d",
                    text, chatId
            ));
        }
    }

    private void processingDeleteMessage(DeleteMessage deleteMessage, Long chatId){
        try {
            telegramClient.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(String.format(
                    "Помилка при видаленні повідомлення з id повідомлення %d користувача з id %d",
                     chatId, deleteMessage.getMessageId()
            ));
        }
    }
}
