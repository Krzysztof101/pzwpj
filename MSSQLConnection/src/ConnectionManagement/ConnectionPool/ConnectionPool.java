package ConnectionManagement.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {
    Connection getConnection() throws SQLException;
    boolean releaseConnection(Connection connection) throws SQLException;
    public String getUrl();
    String getUser();
    String getPassword();
    int getNumberOfFreeConnections();
    int getSize();
    void resetConnection(Connection connection) throws SQLException;
    int getDesiredPoolSize();
    void setDesiredPoolSize(int desiredSize) throws SQLException;
}
