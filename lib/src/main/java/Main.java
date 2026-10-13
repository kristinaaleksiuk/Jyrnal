/**
 * Точка входа приложения «Электронный журнал студентов».
 * Создаёт журнал, заполняет его тестовыми данными и запускает меню.
 */
public class Main {
    public static void main(String[] args) {
        Journal journal = new Journal();
        try {
            DemoData.fill(journal);
        } catch (JournalException e) {
            System.out.println("Не удалось загрузить тестовые данные: " + e.getMessage());
        }
        new Menu(journal).run();
    }
}
