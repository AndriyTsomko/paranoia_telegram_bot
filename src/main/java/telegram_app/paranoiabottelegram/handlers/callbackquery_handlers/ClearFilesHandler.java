package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.File;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.FileService;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

import java.util.List;

@Component
public class ClearFilesHandler extends AbstractCallbackQueryHandler {
    private final FileService fileService;

    public ClearFilesHandler(KeyboardService keyboardService,
                             TelegramMessageSender telegramMessageSender,
                             UserStateService userStateService, FileService fileService) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.fileService = fileService;
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        Long chatId = userState.getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        List<File> files = userState.getFiles();
        if (files.isEmpty()) {
            telegramMessageSender.sendMessage(chatId, "Немає файлів");
            return;
        }

        for (File file : files) {
            fileService.delete(file.getId());
        }
        userState.getFiles().clear();

        telegramMessageSender.sendMessage(chatId, getPromptMessage());

        telegramMessageSender.deleteMessage(chatId,messageId);
        telegramMessageSender.sendMessage(chatId, "Оберіть пункт меню", keyboardService.getHomeworkInlineKeyboard(userState));
        userStateService.updateState(userState, getUpdateStateTo());
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_CLEAR_FILES;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_CLEAR_FILES;
    }

    @Override
    protected String getPromptMessage() {
        return "\uD83E\uDDF9Файли успішно очищено";
    }

    @Override
    protected BotState getUpdateStateTo() {
        return BotState.CREATING_HOMEWORK;
    }
}
