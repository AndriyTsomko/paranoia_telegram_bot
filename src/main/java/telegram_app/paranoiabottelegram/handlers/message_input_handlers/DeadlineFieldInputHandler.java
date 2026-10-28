package telegram_app.paranoiabottelegram.handlers.message_input_handlers;

import org.springframework.stereotype.Component;
import telegram_app.paranoiabottelegram.DeadlineParser;
import telegram_app.paranoiabottelegram.handlers.abstract_classes.AbstractFieldInputMessageHandler;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.services.UserStateService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

import java.util.Date;
import java.util.Optional;

@Component
public class DeadlineFieldInputHandler extends AbstractFieldInputMessageHandler {
    public DeadlineFieldInputHandler(KeyboardService keyboardService,
                                     TelegramMessageSender telegramMessageSender,
                                     UserStateService userStateService) {
        super(keyboardService, telegramMessageSender, userStateService);
    }

    @Override
    protected BotState getTargetState() {
        return BotState.AWAITING_DEADLINE;
    }

    @Override
    protected void applyValue(UserState state, String value) {
        Optional<Date> optionalDate = DeadlineParser.parseToDate(value);
        if (optionalDate.isPresent()){
            state.setDeadline(optionalDate.get());
        } else {
            String errorParsingDateText = "Дату введено некоректно";
            telegramMessageSender.sendMessage(state.getChatId(), errorParsingDateText);
        }
    }
}
