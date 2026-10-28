package telegram_app.paranoiabottelegram;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class DeadlineParser {

    private static final Locale UKRAINIAN_LOCALE = new Locale("uk", "UA");

    private static final DateTimeFormatter FLEXIBLE_FORMATTER = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            .appendOptional(DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm", UKRAINIAN_LOCALE))
            .appendOptional(DateTimeFormatter.ofPattern("d MMMM yyyy 'року' HH:mm", UKRAINIAN_LOCALE))
            .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("d MMMM yyyy", UKRAINIAN_LOCALE))
            .appendOptional(DateTimeFormatter.ofPattern("d MMMM yyyy 'року'", UKRAINIAN_LOCALE))
            .appendOptional(DateTimeFormatter.ofPattern("d MMMM", UKRAINIAN_LOCALE))
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 23)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 59)
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(UKRAINIAN_LOCALE);

    public static Optional<Date> parseToDate(String input) {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }
        try {
            String normalizedInput = input.trim().replaceAll("\\s+", " ").toLowerCase(UKRAINIAN_LOCALE);
            LocalDateTime localDateTime = LocalDateTime.parse(normalizedInput, FLEXIBLE_FORMATTER);
//            if (localDateTime.isBefore(LocalDateTime.now())) {
//                return Optional.empty();
//            }
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            return Optional.of(date);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}