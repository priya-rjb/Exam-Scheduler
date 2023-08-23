/**
 * Student class that represents a student's information
 *
 */
public class Student {
    private String name;
    private String[] courses;

    /**
     *  Constructs an object representing a single student's information
     * @param studentName A string representing a student's name
     * @param studentCourse An Array of strings representing the courses a student is taking
     */
    public Student(String studentName, String[] studentCourse) {
        this.name = studentName;
        this.courses = studentCourse;
    }
    /**
     * Accessor method for the student's name
     * @return A string representing the student's name
     */
    public String getNames() {
        return this.name;
    }
    /**
     * Accessor method for the courses a student is taking
     * @return An Array of strings representing the courses a student is taking
     */
    public String[] getCourses() {
        return this.courses;
    }
}
