package kindergarten;

/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    private int numberOfStudents; 

    private int rows; 
    private int columns; 

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    
    public void makeClassroom ( String filename ) {
        
        StdIn.setFile(filename);
        numberOfStudents = StdIn.readInt();

        //Make an array of students
        Student[] students = new Student[numberOfStudents];
        for (int i = 0; i < numberOfStudents; i++) {
            students[i] = new Student(StdIn.readString(), StdIn.readString(), StdIn.readInt());
        }

        //Alphebatize Students
        Student[] alphStudents = new Student[numberOfStudents];
        for (int i = 0; i < numberOfStudents; i++) {
            //Merge Sort Algorithm
            mergeSort(students, 0, numberOfStudents-1);
        }

        //Place Students Into studentsInLine
        SNode temp = new SNode();
        temp.setStudent(students[0]);
        studentsInLine = temp; 

        SNode currStudent = studentsInLine; 

        for (int i = 1; i < numberOfStudents; i++){
            SNode tempStudent = new SNode();
            tempStudent.setStudent(students[i]);
            currStudent.setNext(tempStudent);
            currStudent = currStudent.getNext();

        }

    }

    private void mergeSort (Student array[], int left, int right) {
        if (left < right) { //Array is greater than 1 element
            int middle = (left + right)/2; 
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            mergeElements(array, left, middle, right);
        }
    }

    private void mergeElements (Student array[], int l, int m, int r) {
        int num1 = m - l + 1; 
        int num2 = r - m; 

        Student Left[] = new Student[num1];
        Student Middle[] = new Student[num2];

        for (int i = 0; i < num1; i++)
        Left[i] = array[l + i];
        for (int j = 0; j < num2; j++)
        Middle[j] = array[m + 1 + j];

        int i, j, k; 
        i = 0; //counter for one side array
        j = 0; //counter for other side array
        k = l; //counter for total elements so always in bounds

        while (i < num1 && j < num2){
            if (Left[i].compareNameTo(Middle[j]) < 0){
                array[k] = Left[i];
                i++;
            } else {
                array[k] = Middle[j];
                j++;
            }
            k++;
        }

        while (i < num1) {
            array[k] = Left[i];
            i++;
            k++;
          }
      
          while (j < num2) {
            array[k] = Middle[j];
            j++;
            k++;
          }
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        StdIn.setFile(seatingChart);
        rows = StdIn.readInt();
        columns = StdIn.readInt();

        seatingAvailability = new boolean[rows][columns];

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                seatingAvailability[i][j] = StdIn.readBoolean();
            }
        }

        studentsSitting = new Student[rows][columns];
	// WRITE YOUR CODE HERE
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {
        if (musicalChairs != null){
            SNode firstStudent = musicalChairs;
            firstStudent.setNext(studentsInLine);
            studentsInLine = firstStudent; 
        }

        SNode ptr = studentsInLine; 

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                if (seatingAvailability[i][j] && ptr != null){
                        studentsSitting[i][j] = ptr.getStudent();
                        ptr = ptr.getNext();
                }
            }
        }
        studentsInLine = null;

	// WRITE YOUR CODE HERE
	
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        
        SNode temp = findFirstStudent();
        musicalChairs = temp;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (studentsSitting[i][j] != null ){
                        SNode tempStudent = new SNode();
                        tempStudent.setStudent(studentsSitting[i][j]);
                        studentsSitting[i][j] = null;
                        musicalChairs.setNext(tempStudent);
                        musicalChairs = musicalChairs.getNext();
                }
            }
        }
        
        musicalChairs.setNext(temp);
        musicalChairs.setNext(musicalChairs.getNext().getNext());

     }

     private SNode findFirstStudent() {
        SNode temp = new SNode();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (studentsSitting[i][j] != null){
                    temp = new SNode(studentsSitting[i][j], null);
                    musicalChairs = temp; 
                    return temp;
                }
            }
        }

        return null; 
     }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {
     //Currently first one
        SNode prev = musicalChairs; //currently last one
        Student[] heightStudents = new Student[numberOfStudents-1];

        int total = numberOfStudents; 
        int random; 
        
        for (int i = 0; i < numberOfStudents -1; i++) {
            random = StdRandom.uniform(total);
            SNode ptr = musicalChairs;

            for (int j = 0; j < random; j++){
                ptr = ptr.getNext();
            }

            // System.out.println(random);
            // System.out.print(" " + ptr.getStudent().getFullName());
            heightStudents[i] = ptr.getNext().getStudent();
            if(ptr.getNext() == musicalChairs){
                ptr.setNext(ptr.getNext().getNext());
                musicalChairs = ptr;
            }
            else ptr.setNext(ptr.getNext().getNext());
            total--; 
    } 



    // System.out.println(heightStudents.length);
    // for (int i = 0; i < heightStudents.length; i++){
    //     System.out.println(heightStudents[i].getFullName());
    // }
    mergeSortHeight(heightStudents, 0, numberOfStudents - 2);

     SNode temp = new SNode();
     temp.setStudent(heightStudents[0]);
     studentsInLine = temp; 

     SNode currStudent = studentsInLine; 

     for (int i = 1; i < numberOfStudents - 1; i++){
         SNode tempStudent = new SNode();
         tempStudent.setStudent(heightStudents[i]);
         currStudent.setNext(tempStudent);
         currStudent = currStudent.getNext();
     }

     seatStudents();
     musicalChairs = null; 
}

private void mergeSortHeight (Student array[], int left, int right) {
    if (left < right) { //Array is greater than 1 element
        int middle = (left + right)/2; 
        mergeSortHeight(array, left, middle);
        mergeSortHeight(array, middle + 1, right);
        mergeElementsHeight(array, left, middle, right);
    }
}

private void mergeElementsHeight (Student array[], int l, int m, int r) {
    int num1 = m - l + 1; 
    int num2 = r - m; 

    Student Left[] = new Student[num1];
    Student Middle[] = new Student[num2];

    for (int i = 0; i < num1; i++)
    Left[i] = array[l + i];
    for (int j = 0; j < num2; j++)
    Middle[j] = array[m + 1 + j];

    int i, j, k; 
    i = 0; //counter for one side array
    j = 0; //counter for other side array
    k = l; //counter for total elements so always in bound

    while (i < num1 && j < num2){
        if (Left[i].getHeight() < Middle[j].getHeight()){
            array[k] = Left[i];
            i++;
        } else {
            array[k] = Middle[j];
            j++;
        }
        k++;
    } 

    while (i < num1) {
        array[k] = Left[i];
        i++;
        k++;
      }
  
      while (j < num2) {
        array[k] = Middle[j];
        j++;
        k++;
      }
}

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {
        
        SNode newStudent = new SNode(new Student(firstName, lastName, height), null);
        // WRITE YOUR CODE HERE
        if (musicalChairs != null){
            newStudent.setNext(musicalChairs.getNext());
            musicalChairs.setNext(newStudent);
            musicalChairs = newStudent;
        }

        else if (studentsInLine != null) {
            SNode ptr = studentsInLine; 
            while (ptr.getNext() != null) {
                ptr = ptr.getNext(); 
            }
            ptr.setNext(newStudent);
        }

        else {
            boolean student = false; 
            if (studentsSitting == null) 
            return; 
            for (int i = 0; i < rows; i++){
                for (int j = 0; j < columns; j++){
                    if(studentsSitting[i][j] != null)
                    student = true; 
                }
            }

            if (student == true){
                for (int i = 0; i < rows; i++){
                    for (int j = 0; j < columns; j++){
                        if (seatingAvailability[i][j] != false && studentsSitting[i][j] == null) {
                            studentsSitting[i][j] = new Student(firstName, lastName, height);
                            return; 
                        }
                    }
                }
            }
        }
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {

        if (musicalChairs != null) {

            SNode first = musicalChairs; 

            SNode ptr = musicalChairs.getNext();
            SNode prev = musicalChairs; 

            while (ptr != musicalChairs){
                
                if (ptr.getStudent().getFirstName().compareToIgnoreCase(firstName) == 0 && ptr.getStudent().getLastName().compareToIgnoreCase(lastName) == 0) {
                    break;
                }
                prev = ptr; 
                ptr = ptr.getNext();
            }

            if (musicalChairs.getStudent().getFirstName().compareToIgnoreCase(firstName) == 0 && musicalChairs.getStudent().getLastName().compareToIgnoreCase(lastName) == 0) {
                while (prev.getNext() != musicalChairs){
                    prev = prev.getNext();
                }
                first = musicalChairs.getNext();
                ptr = musicalChairs; 
            }
            
            prev.setNext(ptr.getNext().getNext());
            musicalChairs = first; 

        }

        else if (studentsInLine != null) {
            SNode ptr = studentsInLine;
            SNode prev = null;

            if(studentsInLine.getStudent().getFirstName().compareToIgnoreCase(firstName) == 0 && studentsInLine.getStudent().getLastName().compareToIgnoreCase(lastName) == 0 && studentsInLine.getNext() == null) {
                studentsInLine = null; 
                return;
            }

            if(studentsInLine.getStudent().getFirstName().compareToIgnoreCase(firstName) == 0 && studentsInLine.getStudent().getLastName().compareToIgnoreCase(lastName) == 0) {
                studentsInLine = studentsInLine.getNext(); 
                return;
            }

            while (ptr.getNext() != null){
                if (ptr.getStudent().getFirstName().compareToIgnoreCase(firstName) == 0 && ptr.getStudent().getLastName().compareToIgnoreCase(lastName) == 0) {
                    break;
                }
                prev = ptr; 
                ptr = ptr.getNext();
            }

            prev.setNext(ptr.getNext());
        }

        else {
            boolean student = false; 
            if (studentsSitting == null) 
            return; 
            for (int i = 0; i < rows; i++){
                for (int j = 0; j < columns; j++){
                    if(studentsSitting[i][j] != null)
                    student = true; 
                }
            }

            if (student == true){
                for (int i = 0; i < rows; i++){
                    for (int j = 0; j < columns; j++){
                        if (seatingAvailability[i][j] = true && studentsSitting[i][j] != null && studentsSitting[i][j].getFirstName().compareToIgnoreCase(firstName) == 0 && studentsSitting[i][j].getLastName().compareToIgnoreCase(lastName) == 0){
                            studentsSitting[i][j] = null;
                        }
                    }
                }
            }
        }
    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else {stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
