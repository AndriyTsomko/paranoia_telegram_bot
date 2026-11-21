package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.HomeworkPrinter;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.File;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.FileService;
import telegram_app.paranoiabottelegram.services.HomeworkService;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

import java.util.List;

@Component
public class ReviewFilesHandler extends AbstractCallbackQueryHandler {

    private final FileService fileService;
    private final HomeworkPrinter homeworkPrinter;
    private final HomeworkService homeworkService;

    public ReviewFilesHandler(KeyboardService keyboardService,
                              TelegramMessageSender telegramMessageSender,
                              UserStateService userStateService, FileService fileService, HomeworkPrinter homeworkPrinter, HomeworkService homeworkService) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.fileService = fileService;
        this.homeworkPrinter = homeworkPrinter;
        this.homeworkService = homeworkService;
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        Long chatId = userState.getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String callbackQueryData = callbackQuery.getData();
        int index = callbackQueryData.indexOf(':');
        Long homeworkId = Long.parseLong(callbackQueryData.substring(index + 1));

        telegramMessageSender.deleteMessage(chatId, messageId);

        List<File> files = fileService.findFilesByHomeworkId(homeworkId);
        if (files.isEmpty()) {
            telegramMessageSender.sendMessage(chatId, "Файли не знайдено");
        } else {
            telegramMessageSender.sendMessage(chatId, "", files);
        }

        homeworkPrinter.printHomework(homeworkService.findById(homeworkId).get(), chatId);

    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_REVIEW_FILES;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_REVIEW_FILES;
    }

    @Override
    protected String getPromptMessage() {
        return "";
    }

    @Override
    protected BotState getUpdateStateTo() {
        return BotState.IDLE;
    }

    @Override
    public boolean canHandle(CallbackQuery callbackQuery, UserState userState) {
        String data = callbackQuery.getData();
        return data.startsWith(getAddCallback()) || data.startsWith(getEditCallback());
    }
}
