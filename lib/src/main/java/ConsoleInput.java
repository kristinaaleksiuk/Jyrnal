import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Безопасный ввод с консоли. Повторяет запрос, пока пользователь не введёт
 * корректное значение, поэтому программа не падает при вводе букв вместо чисел.
 */
public class ConsoleInput {
    private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    /** Режим эха (-Djournal.echo=true) печатает введённые строки — нужен для протоколов тестов. */
    private final boolean echo = Boolean.getBoolean("journal.echo");

    private String readLine() {
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("Ввод завершён.");
        }
        String line = scanner.nextLine();
        if (echo) {
            System.out.println(line);
        }
        return line;
    }

    /** Непустая строка. */
    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String line = readLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Ошибка: значение не может быть пустым.");
        }
    }

    /** Строка; пустой ввод означает «оставить прежнее значение». */
    public String readStringOrKeep(String prompt, String current) {
        System.out.print(prompt + " [Enter — оставить «" + current + "»]: ");
        String line = readLine().trim();
        return line.isEmpty() ? current : line;
    }

    /** Целое число в диапазоне [min, max]. */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + ": ");
            String line = readLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                if (max == Integer.MAX_VALUE) {
                    System.out.println("Ошибка: число должно быть не меньше " + min + ".");
                } else {
                    System.out.println("Ошибка: число должно быть от " + min + " до " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    /** Дата в формате ГГГГ-ММ-ДД; пустой ввод — сегодняшняя дата. */
    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " [ГГГГ-ММ-ДД, Enter — сегодня]: ");
            String line = readLine().trim();
            if (line.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(line);
            } catch (DateTimeParseException e) {
                System.out.println("Ошибка: неверный формат даты, пример: 2026-10-13.");
            }
        }
    }

    /** Ответ да/нет. */
    public boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (да/нет): ");
            String line = readLine().trim().toLowerCase();
            if (line.equals("да") || line.equals("д") || line.equals("y")) {
                return true;
            }
            if (line.equals("нет") || line.equals("н") || line.equals("n")) {
                return false;
            }
            System.out.println("Ошибка: ответьте «да» или «нет».");
        }
    }
}
