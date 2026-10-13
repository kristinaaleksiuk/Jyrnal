import java.time.LocalDate;
import java.util.Arrays;

/**
 * Электронный журнал — класс-хранилище.
 * Хранит студентов, предметы и оценки в обычных массивах (без коллекций и
 * обобщений); при заполнении массив расширяется вручную через Arrays.copyOf.
 * Реализует CRUD-операции, расчёт средних баллов, списки отличников и должников.
 */
public class Journal {
    /** Средний балл, начиная с которого студент считается отличником. */
    public static final double EXCELLENT_THRESHOLD = 4.5;
    /** Средний балл ниже этого значения — студент считается должником. */
    public static final double DEBT_THRESHOLD = 3.0;

    private static final int INITIAL_CAPACITY = 4;

    private Student[] students = new Student[INITIAL_CAPACITY];
    private int studentCount = 0;
    private Subject[] subjects = new Subject[INITIAL_CAPACITY];
    private int subjectCount = 0;
    private Grade[] grades = new Grade[INITIAL_CAPACITY];
    private int gradeCount = 0;

    private int nextStudentId = 1;
    private int nextSubjectId = 1;
    private int nextGradeId = 1;

    // ======================= Студенты =======================

    /** Добавляет студента; дубликат (то же ФИО и группа) запрещён. */
    public Student addStudent(String name, String group) throws InvalidDataException {
        Student student = new Student(nextStudentId, name, group);
        for (int i = 0; i < studentCount; i++) {
            if (students[i].equals(student)) {
                throw new InvalidDataException("Такой студент уже есть в журнале: " + students[i].getDescription());
            }
        }
        if (studentCount == students.length) {
            students = Arrays.copyOf(students, students.length * 2);
        }
        students[studentCount++] = student;
        nextStudentId++;
        return student;
    }

    /** Возвращает копию массива всех студентов. */
    public Student[] getStudents() {
        return Arrays.copyOf(students, studentCount);
    }

    public Student findStudentById(int id) throws NotFoundException {
        int index = indexOfStudent(id);
        if (index < 0) {
            throw new NotFoundException("Студент с ID " + id + " не найден.");
        }
        return students[index];
    }

    /** Поиск студентов по части ФИО (без учёта регистра). */
    public Student[] findStudentsByName(String part) {
        String query = part.trim().toLowerCase();
        Student[] result = new Student[studentCount];
        int count = 0;
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getName().toLowerCase().contains(query)) {
                result[count++] = students[i];
            }
        }
        return Arrays.copyOf(result, count);
    }

    public void updateStudent(int id, String name, String group)
            throws NotFoundException, InvalidDataException {
        Student student = findStudentById(id);
        Student candidate = new Student(id, name, group); // проверка данных
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId() != id && students[i].equals(candidate)) {
                throw new InvalidDataException("Студент с такими данными уже существует.");
            }
        }
        student.setName(name);
        student.setGroup(group);
    }

    /** Удаляет студента вместе со всеми его оценками. */
    public void removeStudent(int id) throws NotFoundException {
        int index = indexOfStudent(id);
        if (index < 0) {
            throw new NotFoundException("Студент с ID " + id + " не найден.");
        }
        for (int i = gradeCount - 1; i >= 0; i--) {
            if (grades[i].getStudent().getId() == id) {
                removeGradeAt(i);
            }
        }
        for (int i = index; i < studentCount - 1; i++) {
            students[i] = students[i + 1];
        }
        students[--studentCount] = null;
    }

    private int indexOfStudent(int id) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    // ======================= Предметы =======================

    public Subject addSubject(String name, String teacher) throws InvalidDataException {
        Subject subject = new Subject(nextSubjectId, name, teacher);
        for (int i = 0; i < subjectCount; i++) {
            if (subjects[i].equals(subject)) {
                throw new InvalidDataException("Такой предмет уже есть в журнале: " + subjects[i].getName());
            }
        }
        if (subjectCount == subjects.length) {
            subjects = Arrays.copyOf(subjects, subjects.length * 2);
        }
        subjects[subjectCount++] = subject;
        nextSubjectId++;
        return subject;
    }

    public Subject[] getSubjects() {
        return Arrays.copyOf(subjects, subjectCount);
    }

    public Subject findSubjectById(int id) throws NotFoundException {
        int index = indexOfSubject(id);
        if (index < 0) {
            throw new NotFoundException("Предмет с ID " + id + " не найден.");
        }
        return subjects[index];
    }

    public Subject[] findSubjectsByName(String part) {
        String query = part.trim().toLowerCase();
        Subject[] result = new Subject[subjectCount];
        int count = 0;
        for (int i = 0; i < subjectCount; i++) {
            if (subjects[i].getName().toLowerCase().contains(query)) {
                result[count++] = subjects[i];
            }
        }
        return Arrays.copyOf(result, count);
    }

    public void updateSubject(int id, String name, String teacher)
            throws NotFoundException, InvalidDataException {
        Subject subject = findSubjectById(id);
        Subject candidate = new Subject(id, name, teacher);
        for (int i = 0; i < subjectCount; i++) {
            if (subjects[i].getId() != id && subjects[i].equals(candidate)) {
                throw new InvalidDataException("Предмет с таким названием уже существует.");
            }
        }
        subject.setName(name);
        subject.setTeacher(teacher);
    }

    /** Удаляет предмет вместе со всеми оценками по нему. */
    public void removeSubject(int id) throws NotFoundException {
        int index = indexOfSubject(id);
        if (index < 0) {
            throw new NotFoundException("Предмет с ID " + id + " не найден.");
        }
        for (int i = gradeCount - 1; i >= 0; i--) {
            if (grades[i].getSubject().getId() == id) {
                removeGradeAt(i);
            }
        }
        for (int i = index; i < subjectCount - 1; i++) {
            subjects[i] = subjects[i + 1];
        }
        subjects[--subjectCount] = null;
    }

    private int indexOfSubject(int id) {
        for (int i = 0; i < subjectCount; i++) {
            if (subjects[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    // ======================= Оценки =======================

    /** Выставляет оценку студенту по предмету. */
    public Grade addGrade(int studentId, int subjectId, int value, LocalDate date)
            throws NotFoundException, InvalidDataException {
        Student student = findStudentById(studentId);
        Subject subject = findSubjectById(subjectId);
        Grade grade = new Grade(nextGradeId, student, subject, value, date);
        if (gradeCount == grades.length) {
            grades = Arrays.copyOf(grades, grades.length * 2);
        }
        grades[gradeCount++] = grade;
        nextGradeId++;
        return grade;
    }

    public Grade[] getGrades() {
        return Arrays.copyOf(grades, gradeCount);
    }

    public Grade findGradeById(int id) throws NotFoundException {
        for (int i = 0; i < gradeCount; i++) {
            if (grades[i].getId() == id) {
                return grades[i];
            }
        }
        throw new NotFoundException("Оценка с ID " + id + " не найдена.");
    }

    public Grade[] getGradesOfStudent(int studentId) throws NotFoundException {
        findStudentById(studentId);
        Grade[] result = new Grade[gradeCount];
        int count = 0;
        for (int i = 0; i < gradeCount; i++) {
            if (grades[i].getStudent().getId() == studentId) {
                result[count++] = grades[i];
            }
        }
        return Arrays.copyOf(result, count);
    }

    public Grade[] getGradesOfSubject(int subjectId) throws NotFoundException {
        findSubjectById(subjectId);
        Grade[] result = new Grade[gradeCount];
        int count = 0;
        for (int i = 0; i < gradeCount; i++) {
            if (grades[i].getSubject().getId() == subjectId) {
                result[count++] = grades[i];
            }
        }
        return Arrays.copyOf(result, count);
    }

    public void updateGrade(int id, int newValue) throws NotFoundException, InvalidDataException {
        findGradeById(id).setValue(newValue);
    }

    public void removeGrade(int id) throws NotFoundException {
        for (int i = 0; i < gradeCount; i++) {
            if (grades[i].getId() == id) {
                removeGradeAt(i);
                return;
            }
        }
        throw new NotFoundException("Оценка с ID " + id + " не найдена.");
    }

    private void removeGradeAt(int index) {
        for (int i = index; i < gradeCount - 1; i++) {
            grades[i] = grades[i + 1];
        }
        grades[--gradeCount] = null;
    }

    // ======================= Статистика =======================

    /** Средний балл студента по всем предметам. */
    public double getStudentAverage(int studentId) throws NotFoundException {
        Student student = findStudentById(studentId);
        double avg = calcAverage(student, null);
        if (Double.isNaN(avg)) {
            throw new NotFoundException("У студента " + student.getName() + " пока нет оценок.");
        }
        return avg;
    }

    /** Средний балл студента по одному предмету. */
    public double getStudentSubjectAverage(int studentId, int subjectId) throws NotFoundException {
        Student student = findStudentById(studentId);
        Subject subject = findSubjectById(subjectId);
        double avg = calcAverage(student, subject);
        if (Double.isNaN(avg)) {
            throw new NotFoundException("У студента " + student.getName()
                    + " нет оценок по предмету «" + subject.getName() + "».");
        }
        return avg;
    }

    /** Средний балл по предмету среди всех студентов. */
    public double getSubjectAverage(int subjectId) throws NotFoundException {
        Subject subject = findSubjectById(subjectId);
        int sum = 0;
        int count = 0;
        for (int i = 0; i < gradeCount; i++) {
            if (grades[i].getSubject().getId() == subjectId) {
                sum += grades[i].getValue();
                count++;
            }
        }
        if (count == 0) {
            throw new NotFoundException("По предмету «" + subject.getName() + "» пока нет оценок.");
        }
        return (double) sum / count;
    }

    /** Список отличников: средний балл не ниже EXCELLENT_THRESHOLD. */
    public Student[] getExcellentStudents() {
        Student[] result = new Student[studentCount];
        int count = 0;
        for (int i = 0; i < studentCount; i++) {
            double avg = calcAverage(students[i], null);
            if (!Double.isNaN(avg) && avg >= EXCELLENT_THRESHOLD) {
                result[count++] = students[i];
            }
        }
        return Arrays.copyOf(result, count);
    }

    /** Список должников: средний балл ниже DEBT_THRESHOLD. */
    public Student[] getDebtors() {
        Student[] result = new Student[studentCount];
        int count = 0;
        for (int i = 0; i < studentCount; i++) {
            double avg = calcAverage(students[i], null);
            if (!Double.isNaN(avg) && avg < DEBT_THRESHOLD) {
                result[count++] = students[i];
            }
        }
        return Arrays.copyOf(result, count);
    }

    /** Средний балл студента (по предмету или, если subject == null, по всем); NaN — оценок нет. */
    public double averageOf(Student student) {
        return calcAverage(student, null);
    }

    private double calcAverage(Student student, Subject subject) {
        int sum = 0;
        int count = 0;
        for (int i = 0; i < gradeCount; i++) {
            boolean sameStudent = grades[i].getStudent().getId() == student.getId();
            boolean sameSubject = subject == null || grades[i].getSubject().getId() == subject.getId();
            if (sameStudent && sameSubject) {
                sum += grades[i].getValue();
                count++;
            }
        }
        return count == 0 ? Double.NaN : (double) sum / count;
    }

    // ======================= Полиморфизм =======================

    /** Все студенты и предметы в одном массиве базового типа JournalEntity. */
    public JournalEntity[] getAllEntities() {
        JournalEntity[] all = new JournalEntity[studentCount + subjectCount];
        for (int i = 0; i < studentCount; i++) {
            all[i] = students[i];
        }
        for (int i = 0; i < subjectCount; i++) {
            all[studentCount + i] = subjects[i];
        }
        return all;
    }
}
