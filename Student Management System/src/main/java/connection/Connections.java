package connection;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Connections {

    private final static String driverName="com.mysql.cj.jdbc.Driver";
    private final static String url="jdbc:mysql://localhost/track_subject";
    private final static String password = "";
    private final static String userName = "root";
    private static Connection connection = null;

    public static Connection connection() {

        try {
            Class.forName(driverName);

            connection= DriverManager.getConnection(url, userName, password);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }
}
