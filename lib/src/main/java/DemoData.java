import java.time.LocalDate;

/** Тестовые данные, чтобы приложение можно было показать сразу после запуска. */
public class DemoData {
    private DemoData() {
    }

    public static void fill(Journal journal) throws JournalException {
        journal.addStudent("Иванов Иван", "ИС-21");
        journal.addStudent("Петрова Анна", "ИС-21");
        journal.addStudent("Сидоров Пётр", "ИС-21");
        journal.addStudent("Кузнецова Мария", "ИС-22");
        journal.addStudent("Смирнов Алексей", "ИС-22");
        journal.addStudent("Попова Елена", "ИС-22");
        journal.addStudent("Новиков Дмитрий", "ИС-22"); // без оценок

        journal.addSubject("Математика", "Орлова Н. В.");
        journal.addSubject("Программирование на Java", "Белов А. С.");
        journal.addSubject("Базы данных", "Громов Д. И.");
        journal.addSubject("Физика", "Лебедев К. П.");

        // Оценки: {студент, предмет, значение}
        int[][] data = {
                {1, 1, 5}, {1, 1, 5}, {1, 2, 5}, {1, 2, 4}, {1, 3, 5}, {1, 4, 5},
                {2, 1, 5}, {2, 1, 4}, {2, 2, 5}, {2, 3, 5}, {2, 3, 5}, {2, 4, 4},
                {3, 1, 2}, {3, 1, 3}, {3, 2, 2}, {3, 3, 3}, {3, 4, 2}, {3, 4, 3},
                {4, 1, 4}, {4, 1, 4}, {4, 2, 5}, {4, 3, 4}, {4, 4, 3},
                {5, 1, 3}, {5, 1, 2}, {5, 2, 3}, {5, 3, 2}, {5, 3, 3}, {5, 4, 3},
                {6, 1, 4}, {6, 1, 5}, {6, 2, 4}, {6, 3, 4}
        };
        for (int i = 0; i < data.length; i++) {
            LocalDate date = LocalDate.of(2026, 9, 1).plusDays(i);
            journal.addGrade(data[i][0], data[i][1], data[i][2], date);
        }
    }
}
