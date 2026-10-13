import java.util.Locale;

/**
 * Консольное меню электронного журнала.
 * Все ошибки предметной области (JournalException) перехватываются здесь
 * и выводятся пользователю, не прерывая работу программы.
 */
public class Menu {
    private final Journal journal;
    private final ConsoleInput in = new ConsoleInput();

    public Menu(Journal journal) {
        this.journal = journal;
    }

    /** Главный цикл программы. */
    public void run() {
        try {
            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("===== ЭЛЕКТРОННЫЙ ЖУРНАЛ СТУДЕНТОВ =====");
                System.out.println("1. Студенты");
                System.out.println("2. Предметы");
                System.out.println("3. Оценки");
                System.out.println("4. Средние баллы, отличники, должники");
                System.out.println("5. Все студенты и предметы (полиморфизм)");
                System.out.println("0. Выход");
                int choice = in.readInt("Ваш выбор", 0, 5);
                switch (choice) {
                    case 1: studentsMenu(); break;
                    case 2: subjectsMenu(); break;
                    case 3: gradesMenu(); break;
                    case 4: statsMenu(); break;
                    case 5: showPolymorphism(); break;
                    default: running = false;
                }
            }
            System.out.println("До свидания!");
        } catch (IllegalStateException e) {
            System.out.println();
            System.out.println("Ввод завершён. Работа программы остановлена.");
        }
    }

    // ---------------------------- Студенты ----------------------------

    private void studentsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- Студенты ---");
            System.out.println("1. Добавить студента");
            System.out.println("2. Показать всех студентов");
            System.out.println("3. Найти студента по части ФИО");
            System.out.println("4. Изменить студента");
            System.out.println("5. Удалить студента");
            System.out.println("0. Назад");
            int choice = in.readInt("Ваш выбор", 0, 5);
            try {
                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: printStudents(journal.getStudents()); break;
                    case 3: findStudents(); break;
                    case 4: editStudent(); break;
                    case 5: removeStudent(); break;
                    default: back = true;
                }
            } catch (JournalException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void addStudent() throws JournalException {
        String name = in.readString("ФИО студента");
        String group = in.readString("Группа");
        Student student = journal.addStudent(name, group);
        System.out.println("Добавлен: " + student);
    }

    private void findStudents() {
        String part = in.readString("Часть ФИО для поиска");
        printStudents(journal.findStudentsByName(part));
    }

    private void editStudent() throws JournalException {
        printStudents(journal.getStudents());
        Student current = journal.findStudentById(in.readInt("ID студента", 1, Integer.MAX_VALUE));
        String name = in.readStringOrKeep("Новое ФИО", current.getName());
        String group = in.readStringOrKeep("Новая группа", current.getGroup());
        journal.updateStudent(current.getId(), name, group);
        System.out.println("Изменено: " + current);
    }

    private void removeStudent() throws JournalException {
        printStudents(journal.getStudents());
        Student student = journal.findStudentById(in.readInt("ID студента", 1, Integer.MAX_VALUE));
        if (in.readYesNo("Удалить «" + student.getName() + "» вместе со всеми оценками?")) {
            journal.removeStudent(student.getId());
            System.out.println("Студент удалён.");
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    private void printStudents(Student[] list) {
        if (list.length == 0) {
            System.out.println("Список пуст.");
            return;
        }
        System.out.println(String.format("%-4s %-22s %-8s", "ID", "ФИО", "Группа"));
        for (int i = 0; i < list.length; i++) {
            System.out.println(String.format("%-4d %-22s %-8s",
                    list[i].getId(), list[i].getName(), list[i].getGroup()));
        }
    }

    // ---------------------------- Предметы ----------------------------

    private void subjectsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- Предметы ---");
            System.out.println("1. Добавить предмет");
            System.out.println("2. Показать все предметы");
            System.out.println("3. Найти предмет по части названия");
            System.out.println("4. Изменить предмет");
            System.out.println("5. Удалить предмет");
            System.out.println("0. Назад");
            int choice = in.readInt("Ваш выбор", 0, 5);
            try {
                switch (choice) {
                    case 1: addSubject(); break;
                    case 2: printSubjects(journal.getSubjects()); break;
                    case 3: findSubjects(); break;
                    case 4: editSubject(); break;
                    case 5: removeSubject(); break;
                    default: back = true;
                }
            } catch (JournalException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void addSubject() throws JournalException {
        String name = in.readString("Название предмета");
        String teacher = in.readString("Преподаватель");
        Subject subject = journal.addSubject(name, teacher);
        System.out.println("Добавлен: " + subject);
    }

    private void findSubjects() {
        String part = in.readString("Часть названия для поиска");
        printSubjects(journal.findSubjectsByName(part));
    }

    private void editSubject() throws JournalException {
        printSubjects(journal.getSubjects());
        Subject current = journal.findSubjectById(in.readInt("ID предмета", 1, Integer.MAX_VALUE));
        String name = in.readStringOrKeep("Новое название", current.getName());
        String teacher = in.readStringOrKeep("Новый преподаватель", current.getTeacher());
        journal.updateSubject(current.getId(), name, teacher);
        System.out.println("Изменено: " + current);
    }

    private void removeSubject() throws JournalException {
        printSubjects(journal.getSubjects());
        Subject subject = journal.findSubjectById(in.readInt("ID предмета", 1, Integer.MAX_VALUE));
        if (in.readYesNo("Удалить предмет «" + subject.getName() + "» вместе со всеми оценками?")) {
            journal.removeSubject(subject.getId());
            System.out.println("Предмет удалён.");
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    private void printSubjects(Subject[] list) {
        if (list.length == 0) {
            System.out.println("Список пуст.");
            return;
        }
        System.out.println(String.format("%-4s %-26s %-14s", "ID", "Предмет", "Преподаватель"));
        for (int i = 0; i < list.length; i++) {
            System.out.println(String.format("%-4d %-26s %-14s",
                    list[i].getId(), list[i].getName(), list[i].getTeacher()));
        }
    }

    // ----------------------------- Оценки -----------------------------

    private void gradesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- Оценки ---");
            System.out.println("1. Выставить оценку");
            System.out.println("2. Показать все оценки");
            System.out.println("3. Оценки студента");
            System.out.println("4. Оценки по предмету");
            System.out.println("5. Изменить оценку");
            System.out.println("6. Удалить оценку");
            System.out.println("0. Назад");
            int choice = in.readInt("Ваш выбор", 0, 6);
            try {
                switch (choice) {
                    case 1: addGrade(); break;
                    case 2: printGrades(journal.getGrades()); break;
                    case 3: studentGrades(); break;
                    case 4: subjectGrades(); break;
                    case 5: editGrade(); break;
                    case 6: removeGrade(); break;
                    default: back = true;
                }
            } catch (JournalException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void addGrade() throws JournalException {
        printStudents(journal.getStudents());
        int studentId = in.readInt("ID студента", 1, Integer.MAX_VALUE);
        printSubjects(journal.getSubjects());
        int subjectId = in.readInt("ID предмета", 1, Integer.MAX_VALUE);
        int value = in.readInt("Оценка (" + Grade.MIN_VALUE + "–" + Grade.MAX_VALUE + ")",
                Grade.MIN_VALUE, Grade.MAX_VALUE);
        java.time.LocalDate date = in.readDate("Дата");
        Grade grade = journal.addGrade(studentId, subjectId, value, date);
        System.out.println("Выставлено: " + grade);
    }

    private void studentGrades() throws JournalException {
        printStudents(journal.getStudents());
        int id = in.readInt("ID студента", 1, Integer.MAX_VALUE);
        printGrades(journal.getGradesOfStudent(id));
    }

    private void subjectGrades() throws JournalException {
        printSubjects(journal.getSubjects());
        int id = in.readInt("ID предмета", 1, Integer.MAX_VALUE);
        printGrades(journal.getGradesOfSubject(id));
    }

    private void editGrade() throws JournalException {
        printGrades(journal.getGrades());
        int id = in.readInt("ID оценки", 1, Integer.MAX_VALUE);
        journal.findGradeById(id);
        int value = in.readInt("Новая оценка (" + Grade.MIN_VALUE + "–" + Grade.MAX_VALUE + ")",
                Grade.MIN_VALUE, Grade.MAX_VALUE);
        journal.updateGrade(id, value);
        System.out.println("Изменено: " + journal.findGradeById(id));
    }

    private void removeGrade() throws JournalException {
        printGrades(journal.getGrades());
        int id = in.readInt("ID оценки", 1, Integer.MAX_VALUE);
        journal.removeGrade(id);
        System.out.println("Оценка удалена.");
    }

    private void printGrades(Grade[] list) {
        if (list.length == 0) {
            System.out.println("Оценок нет.");
            return;
        }
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }
    }

    // ------------------------ Статистика и списки ------------------------

    private void statsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("--- Средние баллы и списки ---");
            System.out.println("1. Средний балл студента");
            System.out.println("2. Средний балл по предмету");
            System.out.println("3. Средний балл студента по предмету");
            System.out.println("4. Список отличников (средний балл ≥ " + fmt(Journal.EXCELLENT_THRESHOLD) + ")");
            System.out.println("5. Список должников (средний балл < " + fmt(Journal.DEBT_THRESHOLD) + ")");
            System.out.println("0. Назад");
            int choice = in.readInt("Ваш выбор", 0, 5);
            try {
                switch (choice) {
                    case 1: studentAverage(); break;
                    case 2: subjectAverage(); break;
                    case 3: studentSubjectAverage(); break;
                    case 4: printWithAverage("Отличники", journal.getExcellentStudents()); break;
                    case 5: printWithAverage("Должники", journal.getDebtors()); break;
                    default: back = true;
                }
            } catch (JournalException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void studentAverage() throws JournalException {
        printStudents(journal.getStudents());
        int id = in.readInt("ID студента", 1, Integer.MAX_VALUE);
        double avg = journal.getStudentAverage(id);
        System.out.println("Средний балл студента " + journal.findStudentById(id).getName() + ": " + fmt(avg));
    }

    private void subjectAverage() throws JournalException {
        printSubjects(journal.getSubjects());
        int id = in.readInt("ID предмета", 1, Integer.MAX_VALUE);
        double avg = journal.getSubjectAverage(id);
        System.out.println("Средний балл по предмету «" + journal.findSubjectById(id).getName() + "»: " + fmt(avg));
    }

    private void studentSubjectAverage() throws JournalException {
        printStudents(journal.getStudents());
        int studentId = in.readInt("ID студента", 1, Integer.MAX_VALUE);
        printSubjects(journal.getSubjects());
        int subjectId = in.readInt("ID предмета", 1, Integer.MAX_VALUE);
        double avg = journal.getStudentSubjectAverage(studentId, subjectId);
        System.out.println(journal.findStudentById(studentId).getName() + " — "
                + journal.findSubjectById(subjectId).getName() + ": " + fmt(avg));
    }

    private void printWithAverage(String title, Student[] list) {
        System.out.println(title + ":");
        if (list.length == 0) {
            System.out.println("  таких студентов нет.");
            return;
        }
        for (int i = 0; i < list.length; i++) {
            System.out.println(String.format("  %-20s %-7s средний балл %s",
                    list[i].getName(), list[i].getGroup(), fmt(journal.averageOf(list[i]))));
        }
    }

    // --------------------------- Полиморфизм ---------------------------

    /** Ссылки типа JournalEntity: getType() и getDescription() вызываются полиморфно. */
    private void showPolymorphism() {
        JournalEntity[] all = journal.getAllEntities();
        if (all.length == 0) {
            System.out.println("Журнал пуст.");
            return;
        }
        for (int i = 0; i < all.length; i++) {
            System.out.println(String.format("%-8s #%d  %s", all[i].getType(), all[i].getId(), all[i].getDescription()));
        }
    }

    private static String fmt(double value) {
        return String.format(Locale.forLanguageTag("ru-RU"), "%.2f", value);
    }
}
