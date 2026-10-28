package telegram_app.paranoiabottelegram.handlers.callbackquery_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCallbackQueryHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.DisciplineLecturer;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.DisciplineLecturerService;
import telegram_app.paranoiabottelegram.services.DisciplineService;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
public class ChooseDisciplineHandler extends AbstractCallbackQueryHandler {
    private final DisciplineService disciplineService;
    private final DisciplineLecturerService disciplineLecturerService;

    public ChooseDisciplineHandler(KeyboardService keyboardService,
                                   TelegramMessageSender telegramMessageSender,
                                   UserStateService userStateService, DisciplineService disciplineService, DisciplineLecturerService disciplineLecturerService) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.disciplineService = disciplineService;
        this.disciplineLecturerService = disciplineLecturerService;
    }

    @Override
    public void handle(CallbackQuery callbackQuery, UserState userState) {
        DisciplineLecturer disciplineLecturer = disciplineLecturerService.findByDiscipline(callbackQuery.getData()).get();
        userState.setDisciplineLecturer(disciplineLecturer);
        userState.setState(BotState.CREATING_HOMEWORK);
        userStateService.save(userState);

        Long chatId = userState.getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        telegramMessageSender.deleteMessage(chatId, messageId);
        telegramMessageSender.sendMessage(chatId, "Виберіть пункт меню", keyboardService.getHomeworkInlineKeyboard());
    }

    @Override
    public boolean canHandle(CallbackQuery callbackQuery, UserState userState) {
        return userState.getState().equals(BotState.AWAITING_DISCIPLINE) && find(callbackQuery.getData());
    }

    @Override
    protected String getAddCallback() {
        return null;
    }

    @Override
    protected String getEditCallback() {
        return null;
    }

    @Override
    protected String getPromptMessage() {
        return null;
    }

    @Override
    protected BotState getUpdateStateTo() {
        return null;
    }

    private boolean find(String data){
        return disciplineService.findAll().stream().anyMatch(discipline -> discipline.getName().equals(data));
    }
}
