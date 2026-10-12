package telegram_app.paranoiabottelegram.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import telegram_app.paranoiabottelegram.DeadlineParser;
import telegram_app.paranoiabottelegram.constants.Constants;
import telegram_app.paranoiabottelegram.entity.DisciplineLecturerEntity;
import telegram_app.paranoiabottelegram.entity.HomeworkEntity;
import telegram_app.paranoiabottelegram.models.BotState;
import telegram_app.paranoiabottelegram.models.Homework;
import telegram_app.paranoiabottelegram.models.UserState;
import telegram_app.paranoiabottelegram.repository.mapper.DisciplineLecturerMapper;
import telegram_app.paranoiabottelegram.services.DisciplineLecturerService;
import telegram_app.paranoiabottelegram.services.DisciplineService;
import telegram_app.paranoiabottelegram.services.HomeworkService;
import telegram_app.paranoiabottelegram.services.UserStateService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final KeyboardService keyboardService;
    private final DisciplineService disciplineService;
    private final DisciplineLecturerService disciplineLecturerService;
    private final HomeworkService homeworkService;
    private final UserStateService userStateService;
    private final DisciplineLecturerMapper disciplineLecturerMapper;

    @Override
    @SneakyThrows
    public void consume(Update update) {
        if (!(update.hasMessage() && update.getMessage().hasText())) return;

        String textMessage = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        UserState user = userStateService.getOrCreate(chatId);

        if (textMessage.equals(Constants.START)) {
            toMainMenu(user);
            return;
        }

        switch (user.getState()) {
            case IDLE -> handleMainMenu(user, textMessage);
            case AWAITING_DISCIPLINE -> handleDisciplineChoice(user, textMessage);
            case AWAITING_TITLE -> handleTitleInput(user, textMessage);
            case AWAITING_DESCRIPTION -> handleDescriptionInput(user, textMessage);
            case AWAITING_DEADLINE -> handleDeadlineInput(user, textMessage);
            //case AWAITING_FILE -> handleFileInput(user, update.getMessage().getPhoto()); todo
        }


    }

    @SneakyThrows
    private void handleMainMenu(UserState user, String textMessage) {
        if (textMessage.equals(Constants.ADD_HOMEWORK)) {
            telegramClient.execute(keyboardService.getLecturerKeyboard(user.getChatId(), disciplineLecturerService));
            userStateService.updateState(user, BotState.AWAITING_DISCIPLINE);
        } else if (textMessage.equals(Constants.CHECK_ALL_HOMEWORK)) {
            for (HomeworkEntity homeworkEntity : homeworkService.findAll()) {
                telegramClient.execute(SendMessage.builder()
                        .chatId(user.getChatId())
                        .text(
                                String.format(
                                        "%s\n\n%s\n\n%s\n\nВикладач: %s\nДедлайн: %s \nДодано:@%s",
                                        homeworkEntity.getDisciplineLecturer().getDiscipline().getName(),
                                        homeworkEntity.getTitle(),
                                        homeworkEntity.getDescription(),
                                        homeworkEntity.getDisciplineLecturer().getLecturer().getName(),

                                        new SimpleDateFormat("d MMMM", new Locale("uk", "UA"))
                                                .format(homeworkEntity.getDeadline()),

                                        telegramClient.execute(new GetChat(homeworkEntity.getTelegramId().toString())).getUserName()
                                )
                        )
                        .build());
            }

            toMainMenu(user);
        } else if (textMessage.equals(Constants.CHECK_WEEK_HOMEWORK)) {
            List<Homework> weekHomeworks = homeworkService.findWeekHomeworks();
            for (Homework homeworkEntity : weekHomeworks){
                telegramClient.execute(SendMessage.builder()
                                .chatId(user.getChatId())
                                .text(
                                        String.format(
                                                "%s\n\n%s\n\n%s\n\nВикладач: %s\nДедлайн: %s \nДодано:@%s",
                                                homeworkEntity.getDisciplineLecturer().getDiscipline().getName(),
                                                homeworkEntity.getTitle(),
                                                homeworkEntity.getDescription(),
                                                homeworkEntity.getDisciplineLecturer().getLecturer().getName(),

                                                new SimpleDateFormat("d MMMM", new Locale("uk", "UA"))
                                                        .format(homeworkEntity.getDeadline()),

                                                telegramClient.execute(new GetChat(homeworkEntity.getTelegramId().toString())).getUserName()
                                        )
                                )
                        .build());
            }
            toMainMenu(user);
        } else if (textMessage.equals(Constants.CHECK_TODAY_HOMEWORK)) {
            List<Homework> todayHomeworks = homeworkService.findTodayHomeworks();
            for (Homework homeworkEntity : todayHomeworks){
                telegramClient.execute(SendMessage.builder()
                        .chatId(user.getChatId())
                        .text(
                                String.format(
                                        "%s\n\n%s\n\n%s\n\nВикладач: %s\nДедлайн: %s \nДодано:@%s",
                                        homeworkEntity.getDisciplineLecturer().getDiscipline().getName(),
                                        homeworkEntity.getTitle(),
                                        homeworkEntity.getDescription(),
                                        homeworkEntity.getDisciplineLecturer().getLecturer().getName(),

                                        new SimpleDateFormat("d MMMM", new Locale("uk", "UA"))
                                                .format(homeworkEntity.getDeadline()),

                                        telegramClient.execute(new GetChat(homeworkEntity.getTelegramId().toString())).getUserName()
                                )
                        )
                        .build());
            }
            toMainMenu(user);
        } else {
            telegramClient.execute(SendMessage.builder()
                    .chatId(user.getChatId())
                    .text(String.format("Команду \"%s\" не було знайдено", textMessage))
                    .build());
            toMainMenu(user);
        }
    }


    @SneakyThrows
    private void handleDisciplineChoice(UserState user, String textMessage) {
        Optional<DisciplineLecturerEntity> optionalDisciplineLecturerEntity = disciplineLecturerService.findByDiscipline(textMessage);

        if (optionalDisciplineLecturerEntity.isPresent()) {
            user.setDisciplineLecturer(disciplineLecturerMapper.toModel(optionalDisciplineLecturerEntity.get()));
            userStateService.update(user, user);
            userStateService.updateState(user, BotState.AWAITING_TITLE);
            telegramClient.execute(SendMessage.builder()
                    .chatId(user.getChatId())
                    .text("Введіть заголовок")
                    .build());
        } else {
            toMainMenu(user);
            throw new RuntimeException(String.format("Дисципіна \"%s\" не була знайдена", textMessage));
        }

    }

    @SneakyThrows
    private void handleTitleInput(UserState user, String textMessage) {
        user.setTitle(textMessage);
        userStateService.update(user, user);
        userStateService.updateState(user, BotState.AWAITING_DESCRIPTION);
        telegramClient.execute(SendMessage.builder().chatId(user.getChatId()).text("Введіть опис").build());
    }

    @SneakyThrows
    private void handleDescriptionInput(UserState user, String textMessage) {
        user.setDescription(textMessage);
        userStateService.update(user, user);
        userStateService.updateState(user, BotState.AWAITING_DEADLINE);
        telegramClient.execute(SendMessage.builder().chatId(user.getChatId()).text("Введіть дедлайн").build());
    }

    @SneakyThrows
    private void handleDeadlineInput(UserState user, String textMessage) {
        Optional<Date> optionalDate = DeadlineParser.parseToDate(textMessage);

        if (optionalDate.isEmpty()){
            toMainMenu(user);
            throw new RuntimeException("Дата введена некоректно");
        }

        user.setDeadline(optionalDate.get());

        userStateService.update(user, user);

        Homework model = Homework.builder()
                .disciplineLecturer(user.getDisciplineLecturer())
                .createdTime(LocalDateTime.now())
                .title(user.getTitle())
                .description(user.getDescription())
                .telegramId(user.getChatId())
                .deadline(user.getDeadline())
                .build();
        homeworkService.save(model);

        telegramClient.execute(SendMessage.builder().chatId(user.getChatId()).text("ДЗ успішно збережено!").
                build());
        toMainMenu(user);
    }

    @SneakyThrows
    private void handleFileInput(UserState user, List<PhotoSize> photo) {
        telegramClient.execute(SendMessage.builder().chatId(user.getChatId()).text("Прикріпіть фото за потреби").build());
        //TODO
    }

    @SneakyThrows
    private void toMainMenu(UserState user){
        telegramClient.execute(keyboardService.getKeyboard(user.getChatId()));
        userStateService.updateState(user, BotState.IDLE);
    }
}
