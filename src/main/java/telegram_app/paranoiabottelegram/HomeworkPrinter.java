package telegram_app.paranoiabottelegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import telegram_app.paranoiabottelegram.models.Homework;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.telegram.TelegramMessageSender;

@Component
@RequiredArgsConstructor
public class HomeworkPrinter {
    private final TelegramMessageSender telegramMessageSender;
    private final TelegramClient telegramClient;

    public void printHomework(UserState userState){
        Long chatId = userState.getChatId();

        String discipline = userState.getDisciplineLecturer() != null ?
                userState.getDisciplineLecturer().getDiscipline().getName() : "не вказано";

        String title = userState.getTitle() != null ? userState.getTitle() : "не вказано";

        String description = userState.getDescription() != null ? userState.getDescription() : "не вказано";

        String lecturer = userState.getDisciplineLecturer() != null ?
                userState.getDisciplineLecturer().getLecturer().getName() : "не вказано";

        String deadline = userState.getDeadline() != null ? userState.getDeadline().toString() : "не вказано";

        String homeworkText = String.format(
                "Предмет: %s\n\nЗаголовок: %s\n\nОпис: %s\n\nВикладач: %s\n\nТермін здачі: %s",
                discipline,
                title,
                description,
                lecturer,
                deadline
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

        String deadline = homework.getDeadline() != null ? homework.getDeadline().toString() : "не вказано";
        String username = null;

        try {
            username = telegramClient.execute(new GetChat(homework.getTelegramId().toString())).getUserName();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }



        String homeworkText = String.format(
                "Предмет: %s\n\nЗаголовок: %s\n\nОпис: %s\n\nВикладач: %s\n\nТермін здачі: %s\nДодано: @%s",
                discipline,
                title,
                description,
                lecturer,
                deadline,
                username
        );

        telegramMessageSender.sendMessage(chatId, homeworkText);
    }
    //todo видалити дублювання коду
}
