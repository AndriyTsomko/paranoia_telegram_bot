package telegram_app.paranoiabottelegram.handlers.message_input_handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import telegram_app.paranoiabottelegram.DeadlineParser;
import telegram_app.paranoiabottelegram.HomeworkPrinter;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractFieldInputMessageHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.HomeworkService;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class TwoDateInputHandler extends AbstractFieldInputMessageHandler {
    private final HomeworkService homeworkService;
    private final HomeworkPrinter homeworkPrinter;

    public TwoDateInputHandler(KeyboardService keyboardService,
                               TelegramMessageSender telegramMessageSender,
                               UserStateService userStateService, HomeworkService homeworkService, HomeworkPrinter homeworkPrinter) {
        super(keyboardService, telegramMessageSender, userStateService);
        this.homeworkService = homeworkService;
        this.homeworkPrinter = homeworkPrinter;
    }

    @Override
    public void handle(Message message, UserState userState) {
        String text = message.getText();
        Long chatId = userState.getChatId();

        String[] twoDates = text.split("-");
        if (twoDates.length == 2) {
            Optional<LocalDateTime> firstDate = DeadlineParser.parseToLocalDateTime(twoDates[0]);
            Optional<LocalDateTime> secondDate = DeadlineParser.parseToLocalDateTime(twoDates[1]);

            if (
                    twoDates[0] != null
                            && twoDates[1] != null
                            && firstDate.isPresent()
                            && secondDate.isPresent()
                            && (firstDate.equals(secondDate) || firstDate.get().isBefore(secondDate.get()))
            ) {
                homeworkService.findHomeworksBetweenDates(firstDate.get(), secondDate.get())
                        .forEach(homework -> homeworkPrinter.printHomework(homework, chatId));
            }else {
                telegramMessageSender.sendMessage(chatId, "Дату введено некоректно");
            }

        }else {
            telegramMessageSender.sendMessage(chatId, "Дату введено некоректно");
        }
        userStateService.updateState(userState, BotState.IDLE);
    }

    @Override
    protected BotState getTargetState() {
        return BotState.AWAITING_DEADLINE_BY_TWO_DATES;
    }

    @Override
    protected void applyValue(UserState state, String value) {
    }
}
