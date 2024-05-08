package edu.virginia.sde.reviews;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMainTest {
    public static void main(String[] args) {
        try {
            DatabaseDriver databaseDriver = DatabaseSingleton.getInstance();

            int id = databaseDriver.getNextAvailableId();

            System.out.printf("id: %d%n", id);


            int courseNumber = 3140;
            String subject = "CS";
            String title = "Software Development Essentials";
            List<Review> reviews = new ArrayList<>();
            Course sde = new Course(courseNumber, subject, title, reviews);

            int intRating = 5;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String comment = "Great";
            String username = "student";
            Review review = new Review(id, intRating, timestamp, comment, username, sde);

            databaseDriver.addReview(review);
            databaseDriver.commit();
        } catch (SQLException e) {
            System.out.println("Failed");
            System.out.println(e.getMessage());
        }
    }
}
