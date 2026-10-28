package telegram_app.paranoiabottelegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import telegram_app.paranoiabottelegram.models.Homework;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

import java.time.format.TextStyle;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class HomeworkPrinter {
    private final TelegramMessageSender telegramMessageSender;
    private final TelegramClient telegramClient;

    public void printHomework(UserState userState){
        Long chatId = userState.getChatId();

        String discipline = userState.getDisciplineLecturer() != null ?
                userState.getDisciplineLecturer().getDiscipline().getName() : "не вказано";

        String title = userState.getTitle() != null
                ? userState.getTitle() : "не вказано";

        String description = userState.getDescription() != null
                ? userState.getDescription() : "не вказано";

        String lecturer = userState.getDisciplineLecturer() != null ?
                userState.getDisciplineLecturer().getLecturer().getName() : "не вказано";

        String deadline = userState.getDeadline() != null
                ? userState.getDeadline().toString() : "не вказано";

        String day = userState.getDeadline().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("uk"));

        String capitalized = userState.getDeadline() != null ?
                day.substring(0, 1).toUpperCase() + day.substring(1)
                : "";

        String homeworkText = String.format(
                "Предмет: %s\n\nЗаголовок: %s\n\nОпис: %s\n\nВикладач: %s\n\nТермін здачі: %s %s",
                discipline,
                title,
                description,
                lecturer,
                deadline,
                capitalized
        );

        telegramMessageSender.sendMessage(chatId, homeworkText);
    }

    public void printHomework(Homework homework, Long chatId){

        String discipline = homework.getDisciplineLecturer() != null ?
                homework.getDisciplineLecturer().getDiscipline().getName() : "не вказано";

        String title = homework.getTitle() != null ? homework.getTitle() : "не вказано";

        String description = homework.getDescription() != null ? homework.getDescription() : "не вказано";

        String lecturer = homework.getDisciplineLecturer() != null ?
                homework.getDisciplineLecturer().getLecturer().getName() : "не вказано";

        String deadline = homework.getDeadline() != null ? homework.getDeadline().format(DeadlineParser.DATE_FORMATTER) : "не вказано";
        String username = null;

        try {
            username = telegramClient.execute(new GetChat(homework.getTelegramId().toString())).getUserName();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        String day = homework.getDeadline().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("uk"));

        String capitalized = homework.getDeadline() != null ?
                day.substring(0, 1).toUpperCase() + day.substring(1)
                : "не вказано";

        String homeworkText = String.format(
                "\nПредмет: %s\n\nЗаголовок: %s\n\nОпис: %s\n\nВикладач: %s\n\nТермін здачі: %s %s\nДодано: @%s",
                discipline,
                title,
                description,
                lecturer,
                deadline,
                capitalized,
                username
        );

        telegramMessageSender.sendMessage(chatId, homeworkText);
    }
    //todo видалити дублювання коду
}
