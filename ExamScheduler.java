// IMPLEMENTED THE LAB AND EXTENSIONS ONE, TWO, AND THREE! :)
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import structure5.*;
/**
 * An ExamScheduler class that reads in Student information file
 * and generates a schedule for final exams so that no student has two exams at the same time.
 */
public class ExamScheduler {
    /**
     * Reads the file passed (i.e Student information)
     * Parses through the file, to create a Student object with student details
     * with name, and the courses.
     * @return Vector of Student objects (student details)
     */
    public Vector<Student> readFromFile() {
        Vector<Student> studentList = new Vector<Student>();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            //Reads the name, the four courses
            String[] courses = new String[4];
            String name = in.nextLine();
            courses[0] = in.nextLine();
            courses[1] = in.nextLine();
            courses[2] = in.nextLine();
            courses[3] = in.nextLine();
            //Creates a Student object that stores details of a particular student
            Student student = new Student(name, courses);
            //Adds the particular Student object in a Vector of Student Objects
            studentList.add(student);
        }
        //returns the Vector of Student objects
        return studentList;
    }
    /**
     * Generates a graph of all of the courses students are taking.
     * Edges representing the courses one student is taking.
     * @param studentList Vector of Student objects
     * @return An GraphListUndirected with courses as the vertices
     */
    public GraphListUndirected<String, Integer> generateGraph(Vector<Student> studentList) {
        //Inititialze a graph of courses:
        GraphListUndirected<String, Integer> courseConflicts = new GraphListUndirected<>();
        for (Student stu: studentList) {
            String[] courses = stu.getCourses();
            //Add vertices(courses) to the graph:
            for (int i = 0; i < 4; i++) {
                if (!courseConflicts.contains(courses[i])) {
                    courseConflicts.add(courses[i]);
                }
            }
            //Adding edges (six edges--signfiying that one student is taking these classes):
            for (int i = 1; i < 4; i++) {
                for (int j = 0; j < i; j++) {
                    courseConflicts.addEdge(courses[i], courses[j], 1);
                }
            }
        }
        return courseConflicts;
    }
    /**
     * Adds a course from the the Vector of courses to the slot
     * @param random boolean to check if the user wants to implement extension three
     * @param allClasses Vector of all of the courses
     * @return Vector of String (each slot with courses)
     */
    public Vector<String> checkRandomness(boolean random, Vector<String> allClasses) {
        Vector<String> slots = new Vector<>();
            if (random) {
                //Adds a random course from the Vector of courses to one slot:
                Random r = new Random();
                int j = r.nextInt(allClasses.size());
                slots.add(allClasses.elementAt(j));
            } else {
                //Adds the first course in the Vector of Courses to one slot:
                slots.add(allClasses.getFirst());
            }
        return slots;
    }
    /**
     * Iterates through the graph and generates slots with courses that don't conflict
     * and returns a Vector with slots containing courses
     * @param graph An GraphListUndirected with courses as the vertices and edges(signifying one student taking it)
     * @param random Boolean to check if the user wants to implement extension three
     * @return Vector of Vector of String (Vector of Vector of Slots) i.e. the exam schedule.
     */
    public Vector<Vector<String>> generateSlots(GraphListUndirected<String, Integer> graph, boolean random) {
        Vector<String> allClasses = new Vector<>();
        //Iterates through the graph of courses and creates a Vector of all of the classes:
        for (String course: graph) {
            allClasses.add(course);
        }
        //Iterates until there are still courses in the graph:
        Vector<Vector<String>> allSlots = new Vector<>();
        while (!graph.isEmpty()) {
            //Adds the a course in the Vector of Courses to one slot:
            Vector<String> slots = checkRandomness(random, allClasses);
            //Iterates through all of the courses left:
            for (int i = 0; i < allClasses.size(); i++) {
                boolean conflict = false;
                //Iterates through the courses in each slot:
                for (String slot: slots) {
                    //Checks if the course conflicts with all of the other courses in the slot:
                    if (graph.containsEdge(slot, allClasses.elementAt(i)) || allClasses.elementAt(i).equals(slot)) {
                        conflict = true;
                    }
                }
                //If the course doesn't conflict add it to that particular slot:
                if (!conflict) {
                    slots.add(allClasses.elementAt(i));
                }
            }
            //Adds the slot to the Vector of slots (the exam schedule)
            allSlots.add(slots);
            //Iterating through the courses from one slot:
            //remove the courses that have already been assigned to a slot
            //from the Vector of courses and the graph of courses.
            for (String slottedCourses: slots) {
                graph.remove(slottedCourses);
                allClasses.remove(slottedCourses);
            }
        }
        return allSlots;
    }
    /**
     * Print out a final exam schedule ordered by course name/number.
     * For each course, all the students taking that course are printed.
     * @param graph An GraphListUndirected with courses as the vertices.
     * @param allslots Vector of Vector of String (Vector of Vector of Slots) i.e. the exam schedule.
     * @param studentList Vector of Student objects
     */
    public void extensionOne(GraphListUndirected<String, Integer> graph, Vector<Vector<String>> allslots, Vector<Student> studentList) {
        //Iterates through the graph of courses
        //and generates and orderedVector in the courses in alphabetical order
        OrderedVector<String> alphOrderedVector = new OrderedVector<>();
        Iterator<String> graphIterator = graph.iterator();
        while (graphIterator.hasNext()) {
            alphOrderedVector.add(graphIterator.next());
        }
        //Iterates and prints each course in the OrderedVector:
        for (String orderedcourse: alphOrderedVector) {
            System.out.println(orderedcourse);
            //Gets the slot corresponding to the course:
            System.out.println("Slot: " + getSlot(orderedcourse, allslots));
            //Retrieves the students taking that course:
            System.out.println("Students: " + sendStudents(orderedcourse, studentList) + " \n");
        }
    }
    /**
     * Retrieves the Students taking the course
     * @param course String representing the course a student is taking
     * @param studentList Vector of Student objects
     * @return String of students taking that course
     */
    public String sendStudents(String course, Vector<Student> studentList) {
        String students = "";
        //Iterates through each student in the Vector of Student object:
        for (Student student: studentList) {
            String[] courseArray = student.getCourses();
            //Iterates through all of the courses that student is taking:
            for (int i = 0; i < 4; i++) {
                //Checks if the course we passed matches one of the courses the student is taking:
                if (courseArray[i].equals(course)) {
                    students += "  " + student.getNames();
                }
            }
        }
        return students;
    }
    /**
     * Prints out a final exam schedule for each student, listing students in alphabetical order.
     * For each student, the exam slots they should attend is listed.
     * @param studentList  Vector of Student objects
     * @param graph An GraphListUndirected with courses as the vertices.
     * @param allslots Vector of Vector of String (Vector of Vector of Slots) i.e. the exam schedule.
     */
    public void extensionTwo(Vector<Student> studentList, GraphListUndirected<String, Integer> graph, Vector<Vector<String>> allslots) {
        //Initialize an OrderedVector that will hold a ComparableAssociation,
        //with the the names as the keys and the courses a student is taking as the value.
        //This OrderedVector is alphabetically ordered based on a student's name
        OrderedVector<ComparableAssociation<String, String[]>> orderedStudent = new OrderedVector<>();
        //Iterates through every student in the Vector of Students
        for (Student student: studentList) {
            //Adds the studentname(key) and the array of courses the student is taking:
            orderedStudent.add(new ComparableAssociation<String, String[]>(student.getNames(), student.getCourses()));
        }
        //Iterates through every Association in the Vector:
        for (ComparableAssociation<String, String[]> studentData: orderedStudent) {
            //Prints the student name:
            System.out.println(studentData.getKey() + ": ");
            //Iterates through all of the student's courses:
            for (String course: studentData.getValue()) {
                //Prints the Slot corresponding to that course:
                System.out.println("Slot: " + getSlot(course, allslots));
            }
        }
    }
    /**
     * Retrieves the slot corresponding to the course in the exam schedule.
     * @param course String representing the course to check
     * @param allslots Vector of Vector of String (Vector of Vector of Slots) i.e. the exam schedule.
     * @return Integer of the slot
     */
    public int getSlot(String course, Vector<Vector<String>> allslots) {
        //Iterates through slots in the exam schedule:
        for (int i = 0; i < allslots.size(); i++) {
            //Checks if the slot contains that course
            if (allslots.elementAt(i).contains(course)) {
                //returns the index of the slots (+1):
                return i + 1;
            }
        }
        //if the course isn't found in the slot:
        return 0;
    }
    /**
     * Prints the final exam schedule (slots and the courses associated with them)
     * @param allslots Vector of Vector of Courses (Exam schedule with all of the slots)
     */
    public void printSlots(Vector<Vector<String>> allslots) {
        int i = 1;
        //Iterates through the Exam Schedule:
        for (Vector<String> slots: allslots) {
            System.out.print("Slot " + i++ + ": ");
            //Iterates through the courses of each slot:
            for (String eachSlot: slots) {
                System.out.print(" " + eachSlot + " ");
            }
            System.out.println();
        }
    }
    /**
     * As output, print the largest and smallest solutions(final exam schedules) found in a given run.
     * @param differentSolutions Vector of (Vector of Vector of String) i.e. A Vector of different permutations of the exam schedule.
     */
    public void extensionThree(Vector<Vector<Vector<String>>> differentSolutions) {
        Vector<Vector<String>> bestSchedule = differentSolutions.getFirst();
        Vector<Vector<String>> worstSchedule = differentSolutions.getFirst();
        //Iterates through different permutations of the exam schedule:
        for (Vector<Vector<String>> permutationOfSlots: differentSolutions) {
            //If the ExamSchedule was lesser slots than the best schedule,
            //update the exam schedule:
            if (permutationOfSlots.size() < bestSchedule.size()) {
                bestSchedule = permutationOfSlots;
            }
            //If the ExamSchedule was great slots than the best schedule,
            //update the worst exam schedule:
            if (permutationOfSlots.size() > worstSchedule.size()) {
                worstSchedule = permutationOfSlots;
            }
        }
        System.out.println("Smallest Schedule:");
        printSlots(bestSchedule);
        System.out.println("Largest Schedule:");
        printSlots(worstSchedule);
    }
    /**
     * To run our exam scheduler to read in as input a Student detail file and generate a schedule.
     * Runs Extension One, Two, and Three.
     * To run Extension Three type: "random" in the argument line
     * ex: java ExamScheduler random < small.txt
     * @param args A String that checks if the user wants to run Extension Three by typing "random"
     */
    public static void main(String[] args) {
            ExamScheduler examSlots = new ExamScheduler();
            //Generates the Vector of Student object:
            Vector<Student> studentLists = examSlots.readFromFile();
            //When no argument is passed and the class is run generally:
            if (args.length == 0) {
                //Generates a graph of courses as vertices and edges(students taking them):
                GraphListUndirected<String, Integer> examgraph = examSlots.generateGraph(studentLists);
                GraphListUndirected<String, Integer> examgraphCopy = examSlots.generateGraph(studentLists);
                //Generates a Vector of Slots (final exam schedule):
                Vector<Vector<String>> examVector = examSlots.generateSlots(examgraph, false);
                System.out.println("Exam Schedule:");
                examSlots.printSlots(examVector);
        
                System.out.println("\nExtension One:");
                examSlots.extensionOne(examgraphCopy, examVector, studentLists);

                System.out.println("\nExtension Two:");
                examSlots.extensionTwo(studentLists, examgraph, examVector);

            } else if (args[0].equals("random")) {
                //TYPE "random" as an argument to run:
                //ExtensionThree:
                //Vector of all the exam schedules(different permutations):
                Vector<Vector<Vector<String>>> differentSolutions = new Vector<>();
                //Runs the program 10 times:
                for (int i = 0; i < 10; i++) {
                    //Generates a graph of courses as vertices and edges(students taking them):
                    GraphListUndirected<String, Integer> randomexamgraph = examSlots.generateGraph(studentLists);
                    //Generates a Vector of slots with courses assigned randomly:
                    Vector<Vector<String>> randomexamVector = examSlots.generateSlots(randomexamgraph, true);
                    //System.out.println("Exam Schedule:");
                    //examSlots.printSlots(examVector);
                    differentSolutions.add(randomexamVector);
                }
                examSlots.extensionThree(differentSolutions);
            }
    }
}