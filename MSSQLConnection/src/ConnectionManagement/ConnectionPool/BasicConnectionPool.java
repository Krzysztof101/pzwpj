package ConnectionManagement.ConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

//variation of example available at: https://www.baeldung.com/java-connection-pooling


public class BasicConnectionPool
        implements ConnectionPoolWithClosing {

    private String url;
    private String user;
    private String password;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections;
    private static int INITIAL_POOL_SIZE = 10;
    private int desiredPoolSize;
    public static BasicConnectionPool create(
            String url, String user,
            String password) throws SQLException {

        return BasicConnectionPool.create(url, user,password, INITIAL_POOL_SIZE);
    }
    public static BasicConnectionPool create(
            String url, String user,
            String password, int poolSize) throws SQLException {


        List<Connection> pool = new LinkedList<>();
        for (int i = 0; i < poolSize; i++) {
            pool.add(createConnection(url, user, password));
        }
        return new BasicConnectionPool(url, user, password, pool);
    }

    private BasicConnectionPool(String url, String user, String password, List<Connection> pool)
    {

        this.desiredPoolSize = pool.size();
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = pool;
        this.usedConnections = new LinkedList<Connection>();
    }

    // standard constructors

    @Override
    public Connection getConnection() throws SQLException {
        if(connectionPool.size()!=0) {
            Connection connection = connectionPool
                    .remove(connectionPool.size() - 1);
            if(!connection.isClosed()) {
                usedConnections.add(connection);
                resetConnection(connection);
                return connection;
            }
            Connection newConnection = BasicConnectionPool.createConnection(url,user,password);
            usedConnections.add(newConnection);
            return newConnection;
        }
        Connection connection = BasicConnectionPool.createConnection(url,user,password);
        usedConnections.add(connection);
        return connection;
    }
    @Override
    public void resetConnection(Connection connection) throws SQLException {
        //TODO add private static connection, that will serve as model to copy it's settings to connection
        // AND write copying code here
    }

    public int getDesiredPoolSize(){return desiredPoolSize;}
    public void setDesiredPoolSize(int desiredSize) throws SQLException {
        if(getSize() < desiredSize)
        {
            while (getSize()<desiredSize)
            {
                connectionPool.add( BasicConnectionPool.createConnection(url,user,password));
            }
            return;
        }
        if(getSize() > desiredSize)
        {
            while (getSize()> desiredSize && connectionPool.size()>0)
            {
                Connection c = connectionPool.remove(0);
                if(!c.isClosed())
                {
                    c.close();
                }
            }

        }
    }

    @Override
    public boolean releaseConnection(Connection connection) throws SQLException {
        if(connection == null || !usedConnections.contains(connection))
        {
            return false;
        }
        var removeConnection = usedConnections.remove(connection);
        usedConnections.remove(connection);
        if(connection.isClosed())
        {
            connection = BasicConnectionPool.createConnection(url,user,password);
        }
        if(getSize()<desiredPoolSize)
        {
            connectionPool.add(connection);
        }
        else
        {
            if(!connection.isClosed())
                connection.close();
        }
        return true;
        /*
        if(removeConnection && !connection.isClosed())
        {
            connectionPool.add(connection);
        }
        else if(removeConnection)
        {
            connectionPool.add(BasicConnectionPool.createConnection(url,user,password));
        }
        return removeConnection;
        */
        //connectionPool.add(connection);
        //return usedConnections.remove(connection);

    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    private static Connection createConnection(
            String url, String user, String password)
            throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }
    @Override
    public int getNumberOfFreeConnections()
    {
        return  connectionPool.size();
    }
    @Override
    public void closeAllConnections() throws SQLException {
        for(Connection c : connectionPool)
        {
            if(c!=null && !c.isClosed())
            {
                c.close();
            }
        }
        connectionPool.clear();
        for (Connection c : usedConnections)
        {
            if(c!=null && !c.isClosed())
            {
                c.close();
            }
        }
        usedConnections.clear();
    }


}