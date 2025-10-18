import java.util.*;

public class CourseGradebook extends Gradebook {

   // TODO: Declare any protected fields here (change placeholder field below)
   /*
    assignmentName: {studentID: grade}
    */
   protected HashMap<String, HashMap<Integer, Double>> assignmentGrade;

   //Constructor
   public CourseGradebook() {
      assignmentGrade = new HashMap<>();
   }

   @Override
   public void setScore(String assignmentName, Integer studentID, Double score) {
      //get the hashmap for the student and their score
      HashMap<Integer, Double> student = assignmentGrade.get(assignmentName);
      if (student == null) { //if student key value is null add new student score
         student = new HashMap<>();
         assignmentGrade.put(assignmentName, student);
      }
      //update score if one already exists for student
      student.put(studentID, score);
   }

   @Override
   public double getScore(String assignmentName, Integer studentID) {
      HashMap<Integer, Double> student = assignmentGrade.get(assignmentName);
      if(student != null){
         return student.get(studentID);
      }
      return Double.NaN;
   }

   @Override
   public HashMap<Integer, Double> getAssignmentScores(String assignmentName) {
      // TODO: Type your code here (remove placeholder line below)
      HashMap<Integer, Double> students = assignmentGrade.get(assignmentName);
      if (students == null){
         return new HashMap<>();
      }
      //We obtain all the key values of students
      return new HashMap<>(students);
   }
   
   @Override
   public ArrayList<String> getSortedAssignmentNames() {
      // TODO: Type your code here (remove placeholder line below)

      ArrayList<String> names = new ArrayList<>(assignmentGrade.keySet());
      Collections.sort(names);
      return names;
   }

   @Override
   public ArrayList<Integer> getSortedStudentIDs() {

      //Get all the assignments
      ArrayList<String> assignments = getSortedAssignmentNames();
      //Create set so we don't duplicate studentIDs
      Set<Integer> studentIDs = new HashSet<>();
      for(String assignment: assignments){ //iterate over all assignments
         HashMap<Integer, Double> map = assignmentGrade.get(assignment);
         studentIDs.addAll(map.keySet()); //add all the studentID's from the assigment
      }
      //Convert from set to an ArrayList
      return new ArrayList<Integer>(studentIDs);

   }

   @Override
   public HashMap<String, Double> getStudentScores(Integer studentID) {
      
       //Get all the assignments
      ArrayList<String> assignments = getSortedAssignmentNames();
      HashMap<String, Double> studentScores = new HashMap<>();
      for(String assignment: assignments){
         Double studentGrade = assignmentGrade.get(assignment).get(studentID);
         if(studentGrade != null){
            studentScores.put(assignment, studentGrade);
         }
      }
      return studentScores;
   }
}