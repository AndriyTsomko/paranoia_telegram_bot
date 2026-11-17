package telegram_app.paranoiabottelegram.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import telegram_app.paranoiabottelegram.models.File;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramMessageSender {
    private final TelegramClient telegramClient;

    public void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .parseMode("HTML")
                .text(text)
                .build();
        processingSendMessage(sendMessage, chatId, text);
    }

    public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .parseMode("HTML")
                .text(text)
                .replyMarkup(replyKeyboard)
                .build();
        processingSendMessage(sendMessage, chatId, text);
    }

    public void sendMessage(Long chatId, String text, List<File> files) {
        if (files == null || files.isEmpty()) {
            sendMessage(chatId, text);
            return;
        }

        List<File> photos = new ArrayList<>();
        List<File> documents = new ArrayList<>();

        for (File file : files) {
            if (isPhoto(file)) {
                photos.add(file);
            } else {
                documents.add(file);
            }
        }

        if (!photos.isEmpty() && documents.isEmpty()) {
            if (photos.size() == 1) {
                sendSinglePhoto(chatId, text, photos.get(0));
            } else {
                sendPhotoGroup(chatId, text, photos);
            }
            return;
        }

        if (photos.isEmpty() && !documents.isEmpty()) {
            if (documents.size() == 1) {
                sendSingleDocument(chatId, text, documents.get(0));
            } else {
                sendDocumentGroup(chatId, text, documents);
            }
            return;
        }


        if (photos.size() == 1) {
            sendSinglePhoto(chatId, text, photos.get(0));
        } else {
            sendPhotoGroup(chatId, text, photos);
        }

        if (documents.size() == 1) {
            sendSingleDocument(chatId, null, documents.get(0));
        } else {
            sendDocumentGroup(chatId, null, documents);
        }
    }

    private boolean isPhoto(File file) {
        String ext = file.getFileExtension();
        if (ext == null) {
            return false;
        }
        return ext.equalsIgnoreCase(".jpg")
                || ext.equalsIgnoreCase(".jpeg")
                || ext.equalsIgnoreCase(".png");
    }

    private void sendSinglePhoto(Long chatId, String caption, File photo) {
        SendPhoto.SendPhotoBuilder builder = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(photo.getTelegramFileId()));

        if (caption != null && !caption.isBlank()) {
            builder.caption(caption).parseMode("HTML");
        }

        try {
            telegramClient.execute(builder.build());
        } catch (TelegramApiException e) {
            throw new RuntimeException("Помилка при відправці фото: " + e.getMessage(), e);
        }
    }

    private void sendSingleDocument(Long chatId, String caption, File document) {
        SendDocument.SendDocumentBuilder builder = SendDocument.builder()
                .chatId(chatId)
                .document(new InputFile(document.getTelegramFileId()));

        if (caption != null && !caption.isBlank()) {
            builder.caption(caption).parseMode("HTML");
        }

        try {
            telegramClient.execute(builder.build());
        } catch (TelegramApiException e) {
            throw new RuntimeException("Помилка при відправці документа: " + e.getMessage(), e);
        }
    }

    private void sendPhotoGroup(Long chatId, String caption, List<File> photos) {
        if (photos.size() <= 10) {
            List<InputMedia> mediaList = new ArrayList<>();
            for (int i = 0; i < photos.size(); i++) {
                InputMediaPhoto mediaPhoto = new InputMediaPhoto(photos.get(i).getTelegramFileId());
                if (i == 0 && caption != null && !caption.isBlank()) {
                    mediaPhoto.setCaption(caption);
                    mediaPhoto.setParseMode("HTML");
                }
                mediaList.add(mediaPhoto);
            }
            SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                    .chatId(chatId)
                    .medias(mediaList)
                    .build();
            try {
                telegramClient.execute(sendMediaGroup);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Помилка при відправці альбому фото: " + e.getMessage(), e);
            }
        } else {
            for (int i = 0; i < photos.size(); i++) {
                sendSinglePhoto(chatId, (i == 0) ? caption : null, photos.get(i));
            }
        }
    }

    private void sendDocumentGroup(Long chatId, String caption, List<File> documents) {
        if (documents.size() <= 10) {
            List<InputMedia> mediaList = new ArrayList<>();
            for (int i = 0; i < documents.size(); i++) {
                InputMediaDocument mediaDoc = new InputMediaDocument(documents.get(i).getTelegramFileId());
                if (i == 0 && caption != null && !caption.isBlank()) {
                    mediaDoc.setCaption(caption);
                    mediaDoc.setParseMode("HTML");
                }
                mediaList.add(mediaDoc);
            }
            SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                    .chatId(chatId)
                    .medias(mediaList)
                    .build();
            try {
                telegramClient.execute(sendMediaGroup);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Помилка при відправці групи документів: " + e.getMessage(), e);
            }
        } else {
            for (int i = 0; i < documents.size(); i++) {
                sendSingleDocument(chatId, (i == 0) ? caption : null, documents.get(i));
            }
        }
    }

    public void deleteMessage(Long chatId, Integer messageId) {
        DeleteMessage deleteUserInputMessage = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
        processingDeleteMessage(deleteUserInputMessage, chatId);
    }

    private void processingSendMessage(SendMessage sendMessage, Long chatId, String text) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(String.format(
                    "Помилка при відправці повідомлення\n\n\"%s\"\n\n до користувача з id %d",
                    text, chatId
            ));
        }
    }

    private void processingDeleteMessage(DeleteMessage deleteMessage, Long chatId) {
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
