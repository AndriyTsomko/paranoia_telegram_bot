package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.DisciplineLecturerService;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class DisciplineHandler extends AbstractCallbackQueryHandler {
    private final DisciplineLecturerService disciplineLecturerService;

    public DisciplineHandler(KeyboardService keyboardService,
                             TelegramMessageSender telegramMessageSender,
                             UserStateService userStateService, DisciplineLecturerService disciplineLecturerService) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.disciplineLecturerService = disciplineLecturerService;
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = userState.getChatId();

        telegramMessageSender.deleteMessage(chatId, messageId);

        telegramMessageSender.sendMessage(chatId, "Оберіть пункт меню",
                keyboardService.getDisciplinesInlineKeyboard(disciplineLecturerService));

        userStateService.updateState(userState, BotState.AWAITING_DISCIPLINE);
    }

    @Override
    protected String getAddCallback() {
        return Constants.CALLBACK_CHOOSE_DISCIPLINE;
    }

    @Override
    protected String getEditCallback() {
        return Constants.CALLBACK_CHOOSE_DISCIPLINE;
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
