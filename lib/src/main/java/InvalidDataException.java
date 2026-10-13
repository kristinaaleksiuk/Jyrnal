/**
 * Исключение: переданы некорректные данные
 * (пустое имя, оценка вне диапазона, дубликат, дата в будущем и т. п.).
 */
public class InvalidDataException extends JournalException {
    private static final long serialVersionUID = 1L;

    public InvalidDataException(String message) {
        super(message);
    }
}
