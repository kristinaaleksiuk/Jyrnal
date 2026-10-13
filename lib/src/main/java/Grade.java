import java.time.LocalDate;

/**
 * Оценка студента по предмету: связывает объекты Student и Subject,
 * хранит значение (2–5) и дату выставления.
 */
public class Grade {
    public static final int MIN_VALUE = 2;
    public static final int MAX_VALUE = 5;

    private final int id;
    private final Student student;
    private final Subject subject;
    private int value;
    private final LocalDate date;

    public Grade(int id, Student student, Subject subject, int value, LocalDate date)
            throws InvalidDataException {
        if (student == null || subject == null) {
            throw new InvalidDataException("Для оценки нужны студент и предмет.");
        }
        if (date == null) {
            throw new InvalidDataException("Не указана дата оценки.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidDataException("Дата оценки не может быть в будущем.");
        }
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.date = date;
        setValue(value);
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) throws InvalidDataException {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new InvalidDataException("Оценка должна быть от " + MIN_VALUE + " до " + MAX_VALUE + ".");
        }
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Оценка #" + id + " [" + date + "] " + student.getName()
                + " — " + subject.getName() + ": " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return id == ((Grade) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
