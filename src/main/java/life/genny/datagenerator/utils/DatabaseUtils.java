package life.genny.datagenerator.utils;

import life.genny.datagenerator.configs.MySQLCatalogueConfig;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.*;

@ApplicationScoped
public class DatabaseUtils {

    private static Logger LOGGER = Logger.getLogger(DatabaseUtils.class);

    private Connection conn;
    private Statement statement;

    public DatabaseUtils() {
    }

    public DatabaseUtils(Connection conn, Statement statement) {
        this.conn = conn;
        this.statement = statement;
    }

    public DatabaseUtils initConnection(MySQLCatalogueConfig mysqlConfig) throws SQLException {
        String url = "jdbc:mysql://" + mysqlConfig.host() + ":" + mysqlConfig.port() + "/" + mysqlConfig.database();
        Connection conn = DriverManager.getConnection(url, mysqlConfig.user(), mysqlConfig.password());
        LOGGER.debug("Connection is created successfully:");
        Statement statement = conn.createStatement();
        return new DatabaseUtils(conn, statement);
    }

    public ResultSet selectAllFromMysql(String table) throws SQLException {
//        if (statement == null) initConnection();
        String query = "SELECT * FROM %s".formatted(table);
        ResultSet result = null;
        result = statement.executeQuery(query);
        return result;
    }

    public void insertIntoMysql(String table, String jsonData) throws SQLException {
//        if (statement == null) initConnection();
        String query = "INSERT INTO `%s`(`json_data`) VALUES ('%s')".formatted(table, jsonData);
        statement.executeUpdate(query);
        conn.close();
    }

    public int getCountFromMysql(String table) throws SQLException {
//        if (statement == null) initConnection();
        int total = -1;
        String query = "SELECT COUNT(id) as total FROM %s".formatted(table);
        ResultSet result = statement.executeQuery(query);
        while (result.next()) total = result.getInt("total");
        conn.close();
        return total;
    }
}
