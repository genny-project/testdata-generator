package life.genny.datagenerator.utils;

import life.genny.datagenerator.configs.MySQLConfig;

import javax.enterprise.context.ApplicationScoped;
import java.sql.*;

@ApplicationScoped
public class DatabaseUtils {

    private static Connection conn;
    private static Statement statement;

    public DatabaseUtils() {
    }

    public DatabaseUtils(Connection conn, Statement statement) {
        DatabaseUtils.conn = conn;
        DatabaseUtils.statement = statement;
    }

    public static DatabaseUtils initConnection(MySQLConfig mysqlConfig) throws SQLException {
        String url = "jdbc:mysql://" + mysqlConfig.host() + ":" + mysqlConfig.port() + "/" + mysqlConfig.database()+"?serverTimezone=UTC";
        Connection conn = DriverManager.getConnection(url, mysqlConfig.user(), mysqlConfig.password());
        Statement statement = conn.createStatement();
        return new DatabaseUtils(conn, statement);
    }

    public static ResultSet selectAllFromMysql(String table) throws SQLException {
        if (conn == null)
            throw new NullPointerException("You have to initialize database connection first.");
        String query = "SELECT * FROM %s".formatted(table);
        ResultSet result = null;
        result = statement.executeQuery(query);
        return result;
    }

    public void insertIntoMysql(String table, String jsonData) throws SQLException {
        String query = "INSERT INTO `%s`(`json_data`) VALUES ('%s')".formatted(table, jsonData);
        statement.executeUpdate(query);
        conn.close();
    }

    public int getCountFromMysql(String table) throws SQLException {
        int total = -1;
        String query = "SELECT COUNT(id) as total FROM %s".formatted(table);
        ResultSet result = statement.executeQuery(query);
        while (result.next()) total = result.getInt("total");
        conn.close();
        return total;
    }
}
