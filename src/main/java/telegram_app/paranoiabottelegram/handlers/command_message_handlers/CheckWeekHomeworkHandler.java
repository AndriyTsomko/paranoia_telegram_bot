package telegram_app.paranoiabottelegram.handlers.command_message_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import telegram_app.paranoiabottelegram.HomeworkPrinter;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractCommandMessageHandler;
import telegram_app.paranoiabottelegram.models.Homework;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.HomeworkService;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

import java.util.List;

@Component
public class CheckWeekHomeworkHandler extends AbstractCommandMessageHandler {
    private final HomeworkService homeworkService;
    private final HomeworkPrinter homeworkPrinter;

    public CheckWeekHomeworkHandler(KeyboardService keyboardService,
                                    TelegramMessageSender telegramMessageSender,
                                    UserStateService userStateService,
                                    HomeworkService homeworkService,
                                    HomeworkPrinter homeworkPrinter) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.homeworkService = homeworkService;
        this.homeworkPrinter = homeworkPrinter;
    }

    @Override
    protected String getTargetCommand() {
        return Constants.CHECK_WEEK_HOMEWORK;
    }

    @Override
    public void handle(Message message, UserState userState) {
        List<Homework> list = homeworkService.findWeekHomeworks();
        if (list.isEmpty()){
            telegramMessageSender.sendMessage(userState.getChatId(), "Не знайдено ДЗ");
        } else {
            list.forEach(homework -> homeworkPrinter.printHomework(homework, userState.getChatId()));
        }
    }
}
