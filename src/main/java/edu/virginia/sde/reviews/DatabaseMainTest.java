//package edu.virginia.sde.reviews;
//
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//
//public class DatabaseMainTest {
//    private static DatabaseDriver databaseDriver;
//    private static final Course c1 = new Course(3140, "CS", "Software Development Essentials", new ArrayList<>());
//
//    private static void addCourses() throws SQLException{
//        databaseDriver.addCourse(c1);
//        Course c2 = new Course(3250, "CS", "Software Testing", new ArrayList<>());
//        databaseDriver.addCourse(c2);
//        Course c3 = new Course(4630, "CS", "Defense Against the Dark Arts", new ArrayList<>());
//        databaseDriver.addCourse(c3);
//    }
//
//    private static void addReviews() throws SQLException{
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        Review r1 = new Review(5, timestamp, "Great class", "1", c1);
//        Review r2 = new Review(3, timestamp, "Too hard", "2", c1);
//        Review r3 = new Review(4, timestamp, "Professor is nice", "3", c1);
//        databaseDriver.addReview(r1);
//        databaseDriver.addReview(r2);
//        databaseDriver.addReview(r3);
//    }
//    public static void main(String[] args) {
//        databaseDriver = DatabaseSingleton.getInstance();
//        // Populates database with three reviews, three courses
////        try {
////            databaseDriver.clearReviews();
////            databaseDriver.clearCourses();
////
////            addCourses();
////            addReviews();
////
////            databaseDriver.commit();
////        } catch (SQLException e){
////            System.out.println("Failure");
////        }
//    }
//}
