package ConnectionManagement.ConnectionPoolManager;

import ConnectionManagement.ConnectionPool.BasicConnectionPool;
import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPool.ConnectionPoolWithClosing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPoolManager implements AutoCloseable, ConnectionPool {
    private static ConnectionPoolWithClosing connectionPool = null;
    private static ConnectionPoolManager manager = null;
    private ConnectionPoolManager()
    {

    }
    public static ConnectionPoolManager getInstance() throws SQLException {
        if(manager != null)
        {
            return manager;
        }
        synchronized (ConnectionPoolManager.class)
        {
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            String url2 = "jdbc:sqlserver://localhost;databaseName=pzwpj_baza;";
            String login = "pzwpj_login";
            String password = "pzwpj_haslo";
            connectionPool = BasicConnectionPool.create(url2, login, password,1);
            manager = new ConnectionPoolManager();
            return manager;
        }

    }


    @Override
    public void close() throws Exception {
        System.out.println("ConnectionPoolManager - auto closeable interface - close");
        connectionPool.closeAllConnections();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    @Override
    public boolean releaseConnection(Connection connection) throws SQLException {
        return connectionPool.releaseConnection(connection);
    }

    @Override
    public String getUrl() {
        return connectionPool.getUrl();
    }

    @Override
    public String getUser() {
        return connectionPool.getUser();
    }

    @Override
    public String getPassword() {
        return connectionPool.getPassword();
    }

    @Override
    public int getNumberOfFreeConnections() {
        return connectionPool.getNumberOfFreeConnections();
    }

    @Override
    public int getSize() {
        return connectionPool.getSize();
    }

    @Override
    public void resetConnection(Connection connection) throws SQLException {
        connectionPool.resetConnection(connection);
    }

}
