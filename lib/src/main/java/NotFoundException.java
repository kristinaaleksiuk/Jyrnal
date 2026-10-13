/**
 * Исключение: запрошенный объект (студент, предмет, оценка)
 * не найден в журнале либо данных для расчёта недостаточно.
 */
public class NotFoundException extends JournalException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
