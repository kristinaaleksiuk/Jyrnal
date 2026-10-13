/**
 * Учебный предмет: название и фамилия преподавателя.
 * Предметы равны, если совпадают названия (без учёта регистра).
 */
public class Subject extends JournalEntity {
    private String teacher;

    public Subject(int id, String name, String teacher) throws InvalidDataException {
        super(id, name);
        setTeacher(teacher);
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) throws InvalidDataException {
        if (teacher == null || teacher.trim().isEmpty()) {
            throw new InvalidDataException("Преподаватель не может быть пустым.");
        }
        this.teacher = teacher.trim();
    }

    @Override
    public String getType() {
        return "Предмет";
    }

    @Override
    public String getDescription() {
        return getName() + ", преподаватель: " + teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return getName().equalsIgnoreCase(((Subject) o).getName());
    }

    @Override
    public int hashCode() {
        return getName().toLowerCase().hashCode();
    }
}
