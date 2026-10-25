package telegram_app.paranoiabottelegram.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.Homework;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.DisciplineLecturerService;
import telegram_app.paranoiabottelegram.services.HomeworkService;
import telegram_app.paranoiabottelegram.services.UserStateService;

import java.util.List;


import static telegram_app.paranoiabottelegram.constants.Constants.*;
import static telegram_app.paranoiabottelegram.models.BotState.AWAITING_DEADLINE;
import static telegram_app.paranoiabottelegram.models.BotState.AWAITING_DESCRIPTION;
import static telegram_app.paranoiabottelegram.models.BotState.AWAITING_DISCIPLINE;
import static telegram_app.paranoiabottelegram.models.BotState.AWAITING_FILE;
import static telegram_app.paranoiabottelegram.models.BotState.AWAITING_TITLE;
import static telegram_app.paranoiabottelegram.models.BotState.IDLE;


@Component
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final KeyboardService keyboardService;
    private final DisciplineLecturerService disciplineLecturerService;
    private final HomeworkService homeworkService;
    private final UserStateService userStateService;

    @SneakyThrows
    @Override
    public void consume(Update update) {
        Long chatId = null;
        UserState user = null;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            user = userStateService.getOrCreate(chatId);
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            chatId = callbackQuery.getMessage().getChatId();

            user = userStateService.getOrCreate(chatId);
            handleCallbackQueryCommands(callbackQuery, user);
        }

        switch (user.getState()) {
            case IDLE -> handleMainMenuCommands(update.getMessage().getText(), chatId);
            case AWA
        }
    }

    private void handleMainMenuCommands(String messageText, Long chatId) {
        switch (messageText) {
            case START -> handleStartCommand(chatId);
            case ADD_HOMEWORK -> {
                deleteKeyboard(chatId);
                handleAddHomeWorkCommand(chatId);
            }
            case CHECK_TODAY_HOMEWORK -> handleCheckTodayHomeworkCommand(chatId);
            case CHECK_WEEK_HOMEWORK -> handleCheckWeekHomeworkCommand(chatId);
            case CHECK_HOMEWORK_BY_OWN_TIME -> handleCheckHomeworkByOwnTime(chatId);
        }
    }

    private void handleCallbackQueryCommands(CallbackQuery callbackQuery, UserState user) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackQueryId = callbackQuery.getId();
        int messageId = callbackQuery.getMessage().getMessageId();

        sendAnswerCallbackQuery(callbackQueryId, "");//todo
        switch (data) {
            case CALLBACK_ADD_TITLE, CALLBACK_EDIT_TITLE -> {
                userStateService.updateState(user, AWAITING_TITLE);
                handleTitleCommand(chatId);
            }
            case CALLBACK_ADD_DESCRIPTION, CALLBACK_EDIT_DESCRIPTION -> {
                userStateService.updateState(user, AWAITING_DESCRIPTION);
                handleDescriptionCommand(chatId);
            }
            case CALLBACK_ADD_DEADLINE, CALLBACK_EDIT_DEADLINE -> {
                userStateService.updateState(user, AWAITING_DEADLINE);
                handleDeadlineCommand(chatId);
            }
            case CALLBACK_ADD_FILE, CALLBACK_EDIT_FILE -> {
                userStateService.updateState(user, AWAITING_FILE);
                handleFileCommand(chatId);
            }
            case CALLBACK_CHOOSE_DISCIPLINE, CALLBACK_EDIT_DISCIPLINE -> {
                userStateService.updateState(user, AWAITING_DISCIPLINE);
                handleDisciplineCommand(chatId);
            }
            case REVIEW_HOMEWORK -> handleReviewHomework(chatId);
            case SAVE_HOMEWORK -> {
                userStateService.updateState(user, IDLE);
                handleSaveHomework(chatId);
            }
            case CANCEL -> {
                userStateService.updateState(user, IDLE);
                handleCancel(chatId);
            }
        }
    }

    private void handleTitleCommand(Long chatId) {
        executeMessage(SendMessage.builder()
                .chatId(chatId)
                .text("Введіть заголовок")
                .build());
    }

    private void handleDescriptionCommand(Long chatId) {
        executeMessage(SendMessage.builder()
                .chatId(chatId)
                .text("Введіть опис")
                .build());
    }

    private void handleDeadlineCommand(Long chatId) {
        executeMessage(SendMessage.builder()
                .chatId(chatId)
                .text("Введіть дедлайн")
                .build());
    }

    private void handleFileCommand(Long chatId) {
        executeMessage(SendMessage.builder()
                .chatId(chatId)
                .text("Прикріпіть фото чи файл")
                .build());
    }

    private void handleDisciplineCommand(Long chatId) {
        InlineKeyboardMarkup disciplinesInlineKeyboard =
                keyboardService.getDisciplinesInlineKeyboard(disciplineLecturerService);
        executeMessage(SendMessage.builder()
                .chatId(chatId)
                .text("Виберіть дисципліну")
                .replyMarkup(disciplinesInlineKeyboard)
                .build());

    }

    private void handleReviewHomework(Long chatId) {

    }

    private void handleSaveHomework(Long chatId) {

    }

    private void handleCancel(Long chatId) {

    }

    private void handleCheckWeekHomeworkCommand(Long chatId) {
        printHomework(homeworkService.findWeekHomeworks(), chatId);
    }

    private void handleCheckTodayHomeworkCommand(Long chatId) {
        printHomework(homeworkService.findTodayHomeworks(), chatId);
    }

    private void handleStartCommand(Long chatId) {
        SendMessage mainMenuMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Оберіть потрібний пункт меню")
                .replyMarkup(keyboardService.getMainKeyboard())
                .build();
        executeMessage(mainMenuMessage);
    }

    private void handleAddHomeWorkCommand(Long chatId) {
        SendMessage mainMenuMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Оберіть потрібний пункт меню")
                .replyMarkup(keyboardService.getHomeworkInlineKeyboard())
                .build();
        executeMessage(mainMenuMessage);

    }

    private void handleCheckHomeworkByOwnTime(Long chatId) {
        //todo
    }

    private void sendAnswerCallbackQuery(String callbackQueryId, String text){
        try {
            telegramClient.execute(AnswerCallbackQuery.builder()
                            .callbackQueryId(callbackQueryId)
                            .text(text)
                            .showAlert(false)
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); //todo
        }
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(
                    String.format(
                            "Не вдалося надіслати повідомлення \n{ %s }\n користувачу з id = %s",
                            sendMessage.getText(),
                            sendMessage.getChatId())
            );
        }
    }

    private String getUsernameByTelegramId(Long chatId) {
        try {
            return telegramClient.execute(new GetChat(chatId.toString())).getUserName();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void printHomework(List<Homework> homeworkList, Long chatId) {
        if (homeworkList.isEmpty()) {
            executeMessage(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text("ДЗ не знайдено")
                            .build()
            );
        }
        homeworkList.forEach(homework -> {
            executeMessage(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(
                                    String.format(
                                            "%s\n%s\n%s\nВикладач: %s\nТермін здачі: %s\nДодано: %s",
                                            homework.getDisciplineLecturer().getDiscipline().getName(),
                                            homework.getTitle(),
                                            homework.getDescription(),
                                            homework.getDisciplineLecturer().getLecturer().getName(),
                                            homework.getDeadline(),
                                            getUsernameByTelegramId(chatId)
                                    )
                            )
                            .build()
            );
        });
    }

    private void deleteKeyboard(Long chatId){
        executeMessage(SendMessage.builder()
                .chatId(chatId)
                .text("Клавіатуру видалено")
                .replyMarkup(keyboardService.replyKeyboardRemove())
                .build());
    }

}
