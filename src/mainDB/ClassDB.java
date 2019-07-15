package mainDB;

import java.sql.*;

public class ClassDB {

    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:employeesDB.db");
        stmt = connection.createStatement();
    }

    public static void disconnect() throws SQLException {
        connection.close();
    }

    public static Connection getConnection() {
        return connection;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static ResultSet returnSQL(String sql, Statement statement) throws SQLException {
        ResultSet result = statement.executeQuery(sql);
        return result;
    }

    public static void voidSQL(String sql) throws SQLException {
        ClassDB.stmt.execute(sql);
    }


}
