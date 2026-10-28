package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.HomeworkPrinter;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class ReviewHomeworkHandler extends AbstractCallbackQueryHandler {
    private final HomeworkPrinter homeworkPrinter;

    public ReviewHomeworkHandler(KeyboardService keyboardService,
                                 TelegramMessageSender telegramMessageSender,
                                 UserStateService userStateService, HomeworkPrinter homeworkPrinter) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.homeworkPrinter = homeworkPrinter;
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        telegramMessageSender.deleteMessage(chatId, messageId);

        homeworkPrinter.printHomework(userState);
        telegramMessageSender.sendMessage(chatId, "Оберіть пункт меню", keyboardService.getHomeworkInlineKeyboard());
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_REVIEW_HOMEWORK;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_REVIEW_HOMEWORK;
    }

    @Override
    protected String getPromptMessage() {
        return null;
    }

    @Override
    protected BotState getUpdateStateTo() {
        return null;
    }
}
