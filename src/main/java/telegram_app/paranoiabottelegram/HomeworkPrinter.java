package telegram_app.paranoiabottelegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import telegram_app.paranoiabottelegram.models.Homework;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.services.KeyboardService;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

import java.time.format.TextStyle;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class HomeworkPrinter {
    private final TelegramMessageSender telegramMessageSender;
    private final TelegramClient telegramClient;
    private final KeyboardService keyboardService;

    public void printHomework(UserState userState) {
        Homework homework = userState.toHomeworkDraft();
        Long chatId = userState.getChatId();
        String homeworkText = getStringFormatHomework(homework, chatId);
        if (homework.getFiles() != null && !homework.getFiles().isEmpty()) {
            telegramMessageSender.sendMessage(chatId, homeworkText, homework.getFiles());
        } else {
            telegramMessageSender.sendMessage(chatId, homeworkText);
        }
    }

    @SneakyThrows
    public void printHomework(Homework homework, Long chatId) {
        String homeworkText = getStringFormatHomework(homework, chatId);
        boolean isCreatorHomework = homework.getTelegramId().equals(chatId);
        Long homeworkId = homework.getId();

        if (homework.getFiles() != null && !homework.getFiles().isEmpty()) {
            if (isCreatorHomework) {
                telegramMessageSender.sendMessage(chatId, homeworkText, keyboardService.getReviewKeyboard(homeworkId));
            } else {
                telegramMessageSender.sendMessage(chatId, homeworkText, homework.getFiles());
            }
        } else {
            if (isCreatorHomework) {
                telegramMessageSender.sendMessage(chatId, homeworkText, keyboardService.getReviewKeyboard(homeworkId));
            }else {
                telegramMessageSender.sendMessage(chatId, homeworkText);
            }
        }

    }

    private String getStringFormatHomework(Homework homework, Long chatId) {
        String discipline = homework.getDisciplineLecturer() != null ?
                homework.getDisciplineLecturer().getDiscipline().getName() : "не вказано";

        String title = homework.getTitle() != null ?
                homework.getTitle() : "не вказано";

        String description = homework.getDescription() != null ?
                homework.getDescription() : "не вказано";

        String lecturer = homework.getDisciplineLecturer() != null ?
                homework.getDisciplineLecturer().getLecturer().getName() : "не вказано";

        String deadline = homework.getDeadline() != null ? homework.getDeadline().format(DeadlineParser.DATE_FORMATTER) : "не вказано";

        String username;

        try {
            username = telegramClient.execute(new GetChat(homework.getTelegramId().toString())).getUserName();
        } catch (TelegramApiException e) {
            telegramMessageSender.sendMessage(chatId, "Помилка");
            throw new RuntimeException(e);
        }

        String day = homework.getDeadline() != null ?
                homework.getDeadline().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("uk")) : "";

        String capitalized = homework.getDeadline() != null ?
                day.substring(0, 1).toUpperCase() + day.substring(1)
                : "";

        return String.format(
                "<b>\uD83D\uDD34Предмет: %s\n\uD83D\uDCC5Термін здачі: %s %s</b>\n\nЗаголовок:\n%s\n\nОпис:\n%s\n\n\uD83D\uDC68\u200D\uD83C\uDFEBВикладач: %s\n\uD83D\uDD17Додано: @%s",
                discipline,
                deadline,
                capitalized,
                title,
                description,
                lecturer,
                username
        );
    }


}
