/**
 * Базовое (проверяемое) исключение электронного журнала.
 * От него наследуются все собственные исключения приложения,
 * поэтому в меню их можно ловить одним блоком catch.
 */
public class JournalException extends Exception {
    private static final long serialVersionUID = 1L;

    public JournalException(String message) {
        super(message);
    }
}
