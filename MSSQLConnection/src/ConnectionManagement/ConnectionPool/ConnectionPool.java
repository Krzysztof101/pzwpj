package ConnectionManagement.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {
    Connection getConnection() throws SQLException;
    boolean releaseConnection(Connection connection) throws SQLException;
    public String getUrl();
    String getUser();
    String getPassword();
    public int getNumberOfFreeConnections();
    int getSize();
    public void resetConnection(Connection connection) throws SQLException;
}
