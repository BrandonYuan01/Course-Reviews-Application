package edu.virginia.sde.reviews;
import java.sql.*;
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
        int id = review.getId();
        int rating = review.getRating();
        String timestamp = review.getTimestamp().toString();
        String comment = review.getComment();
        String username = review.getUsername();
        int courseId = getCourseID(review.getCourse());

        String query = String.format("INSERT INTO Reviews VALUES (%d, %d, \"%s\", \"%s\", \"%s\", %d)", id, rating, timestamp, comment, username, courseId);
        Statement statement = connection.createStatement();

        statement.executeUpdate(query);
        statement.close();
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

    public int getNextAvailableId() throws SQLException{
        try {
            String query = "SELECT id FROM Reviews ORDER BY id DESC LIMIT 1 ";
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);
            statement.close();

            if (rs.next()){
                return rs.getInt("id") + 1;
            }
            else{
                return 0;
            }
        } catch (SQLException e){
            System.out.println("Error getting next available Review Id.");
            throw e;
        }
    }
}

