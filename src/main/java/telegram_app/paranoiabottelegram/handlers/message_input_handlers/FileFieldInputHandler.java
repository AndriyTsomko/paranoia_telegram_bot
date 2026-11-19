package telegram_app.paranoiabottelegram.handlers.message_input_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import telegram_app.paranoiabottelegram.handlers.interfaces.MessageHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.File;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

@Component
public class FileFieldInputHandler implements MessageHandler {
    protected final TelegramMessageSender telegramMessageSender;
    protected final UserStateService userStateService;
    protected final KeyboardService keyboardService;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private final ConcurrentHashMap<String, List<Message>> mediaGroupBuffer = new ConcurrentHashMap<>();

    public FileFieldInputHandler(TelegramMessageSender telegramMessageSender,
                                 UserStateService userStateService,
                                 KeyboardService keyboardService) {
        this.telegramMessageSender = telegramMessageSender;
        this.userStateService = userStateService;
        this.keyboardService = keyboardService;
    }

    @Override
    public boolean canHandle(Message message, UserState userState) {
        return userState.getState().equals(BotState.AWAITING_FILE) && (message.hasDocument() || message.hasPhoto());
    }

    @Override
    public void handle(Message message, UserState userState) {
        String mediaGroupId = message.getMediaGroupId();
        if (mediaGroupId == null) {
            handleFiles(Collections.singletonList(message), userState);
            telegramMessageSender.sendMessage(userState.getChatId(), "Оберіть пункт меню", keyboardService.getHomeworkInlineKeyboard(userState));
        } else {
            handleMediaGroup(mediaGroupId, message, userState);
        }

    }

    public void handleFiles(List<Message> messages, UserState userState) {
        for (Message message : messages) {
            if (message.hasPhoto()) {
                handlePhotos(message, userState);
            } else if (message.hasDocument()) {
                handleDocument(message, userState);
            }
        }
        userState.setState(BotState.CREATING_HOMEWORK);
        userStateService.save(userState);
    }

    public void handlePhotos(Message message, UserState userState) {
        List<PhotoSize> photoSizeList = message.getPhoto();
        PhotoSize photo = photoSizeList.get(photoSizeList.size() - 1);
        File file = File.builder()
                .telegramFileId(photo.getFileId())
                .fileExtension(".jpg")
                .userState(userState)
                .build();
        if (
                userState.getFiles().stream()
                        .anyMatch(f -> f.getTelegramFileId().equals(file.getTelegramFileId()))
        ){
            return;
        }
        userState.getFiles().add(file);
    }

    public void handleDocument(Message message, UserState userState) {
        Document document = message.getDocument();

        String fileId = document.getFileId();
        String fileName = document.getFileName();
        String extension = fileName != null && fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf(".")) : "";

        File file = File.builder()
                .telegramFileId(fileId)
                .fileExtension(extension)
                .userState(userState)
                .build();

        if (
                userState.getFiles().stream()
                        .anyMatch(f -> f.getTelegramFileId().equals(file.getTelegramFileId()))){
            return;
        }

        userState.getFiles().add(file);
    }

    public void handleMediaGroup(String mediaGroup, Message message, UserState user) {
        mediaGroupBuffer.compute(mediaGroup, (key, list) -> {
            if (list == null) {
                list = new CopyOnWriteArrayList<>();
                list.add(message);
                scheduledExecutorService.schedule(() -> {
                    List<Message> messageList = mediaGroupBuffer.remove(mediaGroup);
                    messageList.sort(Comparator.comparingInt(Message::getMessageId));
                    handleFiles(messageList, user);
                    telegramMessageSender.sendMessage(user.getChatId(), "Оберіть пункт меню", keyboardService.getHomeworkInlineKeyboard(user));
                }, 800, TimeUnit.MILLISECONDS);
                return list;
            } else {
                list.add(message);
                return list;
            }
        });
    }
}
