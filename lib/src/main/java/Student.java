/**
 * Студент: ФИО и учебная группа.
 * Два студента считаются одинаковыми, если совпадают ФИО и группа
 * (регистр не учитывается) — это используется для защиты от дубликатов.
 */
public class Student extends JournalEntity {
    private String group;

    public Student(int id, String name, String group) throws InvalidDataException {
        super(id, name);
        // ФИО: только буквы, пробелы, точки и дефисы
        if (!getName().matches("[\\p{L} .\\-]+")) {
            throw new InvalidDataException("ФИО должно содержать только буквы, пробелы, точки и дефисы.");
        }
        setGroup(group);
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) throws InvalidDataException {
        if (group == null || group.trim().isEmpty()) {
            throw new InvalidDataException("Группа не может быть пустой.");
        }
        this.group = group.trim();
    }

    @Override
    public String getType() {
        return "Студент";
    }

    @Override
    public String getDescription() {
        return getName() + ", группа " + group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student other = (Student) o;
        return getName().equalsIgnoreCase(other.getName())
                && group.equalsIgnoreCase(other.group);
    }

    @Override
    public int hashCode() {
        return getName().toLowerCase().hashCode() * 31 + group.toLowerCase().hashCode();
    }
}
