package edu.virginia.sde.reviews;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseDriver {
    private String sqliteFilename;
    private Connection connection;
    private static final String CREATEUSERS = "CREATE TABLE IF NOT EXISTS Users (username TEXT PRIMARY KEY, password TEXT NOT NULL)";
    private static final String CREATECOURSES = "CREATE TABLE IF NOT EXISTS Courses (courseid INTEGER PRIMARY KEY, coursenumber INTEGER NOT NULL, subject TEXT NOT NULL, title TEXT NOT NULL)";
    private static final String CREATEREVIEWS = "CREATE TABLE IF NOT EXISTS Reviews (id INTEGER PRIMARY KEY, rating INTEGER, times TEXT, comment TEXT, username TEXT REFERENCES Users(username), courseid INTEGER REFERENCES Courses(courseid))";

    public DatabaseDriver(String sqlListDatabaseFilename) {
        this.sqliteFilename = sqlListDatabaseFilename;
    }

    public void connect() throws SQLException {
//        if (connection != null && !connection.isClosed()) {
//            throw new IllegalStateException("The connection is already opened");
//        }
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

    public void clearTables() throws SQLException {
        try{
            String deleteReviews = "DELETE FROM Reviews;";
            String deleteCourses = "DELETE FROM Courses;";
            String deleteUsers = "DELETE FROM Users;";
            Statement sqlDeleteReviews = connection.createStatement();
            Statement sqlDeleteCourses = connection.createStatement();
            Statement sqlDeleteUsers = connection.createStatement();
            sqlDeleteReviews.executeUpdate(deleteReviews);
            sqlDeleteCourses.executeUpdate(deleteCourses);
            sqlDeleteUsers.executeUpdate(deleteUsers);
        } catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public void clearReviews() throws SQLException{
        try{
            String deleteReviews = "DELETE FROM Reviews;";
            Statement sqlDeleteReviews = connection.createStatement();
            sqlDeleteReviews.executeUpdate(deleteReviews);
        } catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public void clearCourses() throws SQLException{
        try{
            String deleteCourses = "DELETE FROM Courses;";
            Statement sqlDeleteCourses = connection.createStatement();
            sqlDeleteCourses.executeUpdate(deleteCourses);
        } catch (SQLException e){
            rollback();
            throw e;
        }
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
        preparedStatement.execute();
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

    public void addReview(Review review) throws SQLException{
        String query = "INSERT INTO Reviews (rating, times, comment, username, courseid) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, review.getRating());
        preparedStatement.setString(2, review.getTimestamp().toString());
        preparedStatement.setString(3, review.getComment());
        preparedStatement.setString(4, review.getUsername());
        preparedStatement.setInt(5, getCourseID(review.getCourse()));
        preparedStatement.execute();
        preparedStatement.close();
    }

    public int getReviewID(Review review) throws SQLException {
        int reviewid = -1;
        String query = "SELECT * FROM Reviews WHERE (username, courseid) = (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, review.getUsername());
        preparedStatement.setInt(2, getCourseID(review.getCourse()));

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            reviewid = rs.getInt("id");
        }
        preparedStatement.close();
        return reviewid;
    }

    public void deleteReviewById(int Id) throws SQLException{
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

    public int getReviewIdByUserAndCourse(String username, Course course) throws SQLException{
        int reviewid = -1;
        String query = "SELECT * FROM Reviews WHERE (username, courseid) = (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, getCourseID(course));

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            reviewid = rs.getInt("id");
        }
        preparedStatement.close();
        rs.close();
        return reviewid;
    }

    public String getReviewCommentByUserAndCourse(String username, Course course) throws SQLException{
        String comment = "";
        String query = "SELECT * FROM Reviews WHERE (username, courseid) = (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, getCourseID(course));

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            comment = rs.getString("comment");
        }
        preparedStatement.close();
        rs.close();
        return comment;
    }

    public int getReviewRatingByUserAndCourse(String username, Course course) throws SQLException{
        int rating = -1;
        String query = "SELECT * FROM Reviews WHERE (username, courseid) = (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, getCourseID(course));

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            rating = rs.getInt("rating");
        }
        preparedStatement.close();
        rs.close();
        return rating;
    }
}

