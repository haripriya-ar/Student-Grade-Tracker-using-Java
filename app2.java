import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Inheritance: GradeTrackerApp inherits from JFrame to create a GUI application
public class GradeTrackerApp extends JFrame {
    private JTextField nameField;
    private JTextField idField;
    private JTextField gradeField;
    private JTextArea outputArea;
    private GradeTracker tracker;

    public GradeTrackerApp() {
        super("Student Grade Tracker");
        tracker = new GradeTracker(); // Composition: GradeTrackerApp has a GradeTracker object
        nameField = new JTextField(20);
        idField = new JTextField(10);
        gradeField = new JTextField(5);
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        JButton addButton = new JButton("Add Grade");
        // Polymorphism: Using ActionListener interface to handle button clicks
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int id = Integer.parseInt(idField.getText());
                double grade = Double.parseDouble(gradeField.getText());
                Student student = tracker.findStudentById(id);
                if (student == null) {
                    student = new Student(name, id);
                    tracker.addStudent(student);
                }
                tracker.addGrade(id, grade);

                // Assign ranks after adding a grade
                tracker.assignRanks();

                // Update output area to display updated student information
                updateOutput();
            }
        });

        // Layout using Swing components
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(addButton);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void updateOutput() {
        outputArea.setText("");
        for (Student student : tracker.getStudents()) {
            outputArea.append("Name: " + student.getName() + "\n");
            outputArea.append("ID: " + student.getId() + "\n");
            outputArea.append("Grades: " + student.getGrades() + "\n");
            outputArea.append("Average Grade: " + student.calculateAverageGrade() + "\n");
            outputArea.append("Rank: " + student.getRank() + "\n\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GradeTrackerApp app = new GradeTrackerApp();
            }
        });
    }
}

// Encapsulation: The Student class encapsulates student-related data and methods
class Student {
    private String name;
    private int id;
    private List<Double> grades;
    private int rank;  // Rank of the student

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.grades = new ArrayList<>();
        this.rank = 0;  // Initialize rank to 0 (unranked)
    }

    // Encapsulation: Getter and setter methods to access private fields
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void addGrade(double grade) {
        grades.add(grade);
    }

    // Abstraction: Hides the complexity of calculating the average grade
    public double calculateAverageGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    public List<Double> getGrades() {
        return grades;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

// Encapsulation: The GradeTracker class encapsulates the functionality for managing students and grades
class GradeTracker {
    private List<Student> students;

    public GradeTracker() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addGrade(int studentId, double grade) {
        Student student = findStudentById(studentId);
        if (student != null) {
            student.addGrade(grade);
        }
    }

    public List<Student> getStudents() {
        return students;
    }

    // Abstraction: Hides the complexity of sorting and assigning ranks to students
    public void assignRanks() {
        // Sort students by average grade in descending order
        Collections.sort(students, Comparator.comparingDouble(Student::calculateAverageGrade).reversed());

        // Assign ranks based on sorted order
        for (int i = 0; i < students.size(); i++) {
            students.get(i).setRank(i + 1);
        }
    }

    // Encapsulation: Method to find a student by ID, hides the internal list traversal
    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null; // Return null if student not found
    }
}
