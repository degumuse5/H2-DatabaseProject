package carsharing;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
public class DataBaseManager {
    // Use the connection class to connect to database
    private Connection connection = null;

    // Use the statements to send query's to the database
    private Statement statement = null;
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL =  "jdbc:h2:./src/carsharing/db/carsharing";

    public DataBaseManager () {
        try {
            Class.forName(JDBC_DRIVER);
        }
        catch(ClassNotFoundException e) {
            System.out.print("Error");
        }
    }

    public void connectToDB (){
        try {
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }

    public void createOrDeleteTable(String query) {
        if(query != null && statement != null) {
            try {
                statement.execute(query);
            } catch (SQLException e) {
                System.err.print(e.getMessage());
            }
        }
    }

    public void insertData (String query) {
        if(query != null && statement != null) {
            try {
                statement.executeUpdate(query);
            } catch (SQLException e) {
                System.err.print(e.getMessage());
            }
        }
    }

    public ResultSet pullData (String query) {
        ResultSet set = null;
        if (query != null) {
            try {
                set = statement.executeQuery(query);
            }
            catch (SQLException e) {
                System.err.print(e.getMessage());
            }
        }
        return set;
    }

    public void closeConnection () {
        if(connection != null) {
            try {
                connection.close();
                statement.close();
            }
            catch (SQLException e) {
                System.err.print(e.getMessage());
            }
        }
    }
}
