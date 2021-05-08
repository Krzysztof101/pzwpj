package ConnectionManagement.ConnectionPool;

import java.sql.SQLException;

public interface ConnectionPoolWithClosing extends ConnectionPool {
    void closeAllConnections() throws SQLException;
    //boolean allConnectionsHaveFinishedWork();
}
