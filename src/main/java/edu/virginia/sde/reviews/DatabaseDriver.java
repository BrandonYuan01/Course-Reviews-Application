package edu.virginia.sde.reviews;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDriver {
    private String sqliteFilename;
    private Connection connection;
    private static final String CREATEUSERS = "CREATE TABLE IF NOT EXISTS Users (username TEXT PRIMARY KEY, password TEXT NOT NULL)";
    private static final String CREATECOURSES = "CREATE TABLE IF NOT EXISTS Courses (courseid INTEGER PRIMARY KEY, coursenumber INTEGER NOT NULL, subject TEXT NOT NULL, title TEXT NOT NULL)";
    private static final String CREATEREVIEWS = "CREATE TABLE IF NOT EXISTS Reviews (id INTEGER PRIMARY KEY, rating INTEGER, times TEXT, comment TEXT, username TEXT REFERENCES Users(username), courseid INTEGER REFERENCES Courses(courseid))";

    public DatabaseDriver(String sqlListDatabaseFilename) {
        this.sqliteFilename = sqlListDatabaseFilename;
    }

    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public void connect() throws SQLException {
        if (connection != null && connection.isClosed()) {
            throw new IllegalStateException();
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilename);
        //the next line enables foreign key enforcement - do not delete/comment out
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        //the next line disables auto-commit - do not delete/comment out
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void disconnect() throws SQLException {
        connection.close();
    }
    public void createTables() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATEUSERS);
        statement.execute(CREATECOURSES);
        statement.execute(CREATEREVIEWS);
        statement.close();
    }

    public String getPassword(String username) throws SQLException {
        String userQuery = "SELECT password FROM Users WHERE username = ?";
        String password = null;

        PreparedStatement preparedStatement = connection.prepareStatement(userQuery);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            password = resultSet.getString("password");
        }
        preparedStatement.close();
        resultSet.close();
        return password;
    }

    public void addUser(String username, String password) throws SQLException{
        String query = "INSERT INTO Users (username, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public Boolean checkUser(String username, String password) throws SQLException {
        String query = "SELECT password FROM users WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            preparedStatement.close();
            resultSet.close();
            return password.equals(storedPassword);
        } else {
            preparedStatement.close();
            resultSet.close();
            return false;
        }

    }

    public void addCourse(Course course) throws SQLException {
        String query = "INSERT INTO Courses (coursenumber, subject, title) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, course.getCourseNumber());
        preparedStatement.setString(2, course.getSubject());
        preparedStatement.setString(3, course.getTitle());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public int getCourseID(Course course) throws SQLException {
        int courseid = -1;
        String query = "SELECT * FROM Courses WHERE (coursenumber, subject, title) = (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, course.getCourseNumber());
        preparedStatement.setString(2, course.getSubject());
        preparedStatement.setString(3, course.getTitle());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            courseid = resultSet.getInt("courseid");
        }
        preparedStatement.close();
        resultSet.close();
        return courseid;
    }
    public List<Course> allCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        List<Review> reviews = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Courses");

        while(resultSet.next()) {
            String subject = resultSet.getString("Subject");
            int courseNumber = resultSet.getInt("Number");
            String title = resultSet.getString("Title");

            courses.add(new Course(courseNumber, subject, title, reviews));
        }
        resultSet.close();
        statement.close();
        return courses;
    }
    public List<Review> getReviewsofCourse(Course course) throws SQLException {
        String findReviews = "SELECT * FROM Reviews WHERE CourseID = ?";
        List<Review> reviews = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(findReviews);
        preparedStatement.setInt(1, getCourseID(course));
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()) {
            int Id = resultSet.getInt("CourseID");
            int rating  = resultSet.getInt("Rating");
            String ts = resultSet.getString("Timestamp");
            String comment = resultSet.getString("Review");
            String user = resultSet.getString("UserID");

            Timestamp timestamp = Timestamp.valueOf(ts);
            Review review = new Review(Id, rating, timestamp, comment, user, course);
            reviews.add(review);
        }
        resultSet.close();
        preparedStatement.close();
        return reviews;

    }
    public void addReview(Review review) throws SQLException{
        String query = "INSERT INTO Users (id, rating, times, comment, username, courseid) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, review.getId());
        preparedStatement.setInt(2, review.getRating());
        preparedStatement.setString(3, review.getTimestamp().toString());
        preparedStatement.setString(4, review.getComment());
        preparedStatement.setString(5, review.getUsername());

        Course course = review.getCourse();
        preparedStatement.setInt(6, getCourseID(course));
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void deleteReview(int Id) throws SQLException{
        try {
            String query = String.format("DELETE FROM Reviews WHERE id = %d", Id);
            Statement statement = connection.createStatement();

            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e){
            System.out.println("Error deleting review.");
            throw e;
        }
    }

//    public static void main(String[] args) throws SQLException {
//        DatabaseDriver manager = new DatabaseDriver("database.sqlite");
//        manager.connect();
//        manager.createTables();
//        manager.commit();
//        manager.disconnect();
//    }
}

