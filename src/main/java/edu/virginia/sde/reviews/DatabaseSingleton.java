package edu.virginia.sde.reviews;

import java.sql.SQLException;

public class DatabaseSingleton {
    private static DatabaseSingleton instance;
    private DatabaseDriver databaseDriver;
    private DatabaseSingleton(DatabaseDriver driver) {
        this.databaseDriver = driver;
    }

    public static DatabaseDriver getInstance() {
        try {
            if (instance == null) {
                DatabaseDriver driver = new DatabaseDriver("database.sqlite");
                instance = new DatabaseSingleton(driver);
            }

            if (!instance.databaseDriver.isConnected()) {
                instance.databaseDriver.connect();
            }
        } catch(SQLException e) {
            return null;
        }

        return instance.databaseDriver;
    }
}
