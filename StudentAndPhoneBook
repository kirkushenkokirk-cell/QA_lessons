import java.util.*;

// Задание 1: Студенты
class Student {
    private String name;
    private int group;
    private int course;
    private List<Integer> grades;

    public Student(String name, int group, int course, List<Integer> grades) {
        this.name = name;
        this.group = group;
        this.course = course;
        this.grades = new ArrayList<>(grades);
    }

    public String getName() { return name; }
    public int getCourse() { return course; }
    public void setCourse(int course) { this.course = course; }

    public double getAverageGrade() {
        if (grades.isEmpty()) return 0.0;
        int sum = 0;
        for (int g : grades) sum += g;
        return (double) sum / grades.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return group == student.group && course == student.course && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, group, course);
    }

    @Override
    public String toString() {
        return name + " (группа " + group + ", курс " + course + ", средний балл " + getAverageGrade() + ")";
    }
}

// Задание 2: Телефонный справочник
class PhoneBook {
    private Map<String, List<String>> phoneMap;

    public PhoneBook() {
        phoneMap = new HashMap<>();
    }

    public void add(String surname, String phone) {
        phoneMap.computeIfAbsent(surname, k -> new ArrayList<>()).add(phone);
    }

    public List<String> get(String surname) {
        return phoneMap.getOrDefault(surname, Collections.emptyList());
    }

    public void printAll() {
        System.out.println("Телефонный справочник:");
        for (Map.Entry<String, List<String>> entry : phoneMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

public class Main {


    public static void removeLowAchievers(Set<Student> students) {
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            Student s = iterator.next();
            if (s.getAverageGrade() < 3.0) {
                iterator.remove();
            }
        }
    }

    public static void promoteStudents(Set<Student> students) {
        for (Student s : students) {
            if (s.getAverageGrade() >= 3.0) {
                s.setCourse(s.getCourse() + 1);
            }
        }
    }

    public static void printStudents(Set<Student> students, int course) {
        System.out.println("Студенты на " + course + " курсе:");
        for (Student s : students) {
            if (s.getCourse() == course) {
                System.out.println(s.getName());
            }
        }
    }

    public static void main(String[] args) {
        // ---------- Задание 1 ----------
        Set<Student> students = new HashSet<>();

        students.add(new Student("Алексей Иванов", 101, 1, Arrays.asList(4, 5, 4, 3)));
        students.add(new Student("Мария Петрова", 102, 1, Arrays.asList(2, 3, 2, 2)));
        students.add(new Student("Иван Смирнов", 101, 1, Arrays.asList(5, 5, 5, 5)));
        students.add(new Student("Ольга Кузнецова", 103, 2, Arrays.asList(4, 4, 3, 4)));
        students.add(new Student("Дмитрий Фёдоров", 102, 2, Arrays.asList(2, 2, 3, 2)));

        System.out.println("*** Исходный список студентов ***");
        for (Student s : students) System.out.println(s);

        removeLowAchievers(students);
        System.out.println("\n=== После удаления студентов со средним < 3 ===");
        for (Student s : students) System.out.println(s);

        promoteStudents(students);
        System.out.println("\n=== После перевода на следующий курс ===");
        for (Student s : students) System.out.println(s);

        System.out.println("\n*** Печать студентов 2-го курса ***");
        printStudents(students, 2);

        // ---------- Задание 2 ----------
        System.out.println("\n\n=== Телефонный справочник ===");
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.add("Иванов", "+7-123-456-78-90");
        phoneBook.add("Петров", "+7-987-654-32-10");
        phoneBook.add("Иванов", "+7-111-222-33-44");
        phoneBook.add("Сидоров", "+7-555-666-77-88");

        phoneBook.printAll();
        System.out.println("\nНомера Иванова: " + phoneBook.get("Иванов"));
        System.out.println("Номера Петрова: " + phoneBook.get("Петров"));
        System.out.println("Номера Смирнова (нет): " + phoneBook.get("Смирнов"));
    }
}
