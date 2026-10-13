/**
 * Абстрактная сущность журнала, имеющая идентификатор и название.
 * Базовый класс для Student и Subject. Через ссылку типа JournalEntity
 * демонстрируется полиморфизм: методы getType() и getDescription()
 * у наследников работают по-разному.
 */
public abstract class JournalEntity {
    private final int id;
    private String name;

    protected JournalEntity(int id, String name) throws InvalidDataException {
        this.id = id;
        setName(name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidDataException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Название/имя не может быть пустым.");
        }
        this.name = name.trim();
    }

    /** Название вида сущности ("Студент", "Предмет"). */
    public abstract String getType();

    /** Краткое текстовое описание сущности. */
    public abstract String getDescription();

    @Override
    public String toString() {
        return getType() + " #" + id + ": " + getDescription();
    }
}
