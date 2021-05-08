import ConnectionManagement.ConnectionPool.BasicConnectionPool;
import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPool.ConnectionPoolWithClosing;
import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.Address.AddressDAO;
import model1.Address.Address;
import java.sql.*;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world!");
        //String url = "jdbc:sqlserver://localhost\\SQLEXPRESS";

        /*
        String url = "Data Source=SQLEXPRESS;" +

                "Initial Catalog=pzwpj_baza;" +
                "User id=pzwpj_login;" +
                "pzwpj_haslo";
        Properties properties = new Properties();
        properties.setProperty("Server","localhost\\SQLEXPRESS");
        properties.setProperty("Database","pzwpj_baza");
        properties.setProperty("User Id", "pzwpj_login");
        properties.setProperty("Password", "pzwpj_haslo");
        */
        String url = "jdbc:sqlserver://localhost;databaseName=pzwpj_baza;user=pzwpj_login;password=pzwpj_haslo;";
        //jdbc:sqlserver://localhost;databaseName=AdventureWorks;integratedSecurity=true;

        try {
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            System.out.println("Driver registered successfully");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Failed to register driver");
        }

        Connection conn = null;
        try {
            //conn = DriverManager.getConnection(url,properties);
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Failed to connect to db");
        }
        if (conn != null) {
            System.out.println("Connected");
            try
            {
                int i=1;
                PreparedStatement statement = conn.prepareStatement("insert into pzwpj_schema.addresses(street, buildingNumber,"+
                        " appartmentNumber, appartmentNumberAppendix, city, country, postalCode, region)" +
                        "values (?, ?, ?, ?, ?, ?, ?, ?);");
                statement.setString(i++,"krzywa" );
                statement.setInt(i++,1);
                statement.setInt(i++, 2);
                statement.setNull(i++, Types.NULL);
                statement.setString(i++,"Gdynia");
                //i++;
                statement.setString(i++,"Poland");
                statement.setString(i++,"31-213");
                statement.setString(i++,"pomorskie");

                //var ii = statement.executeUpdate();
                System.out.println("Successfully executed update");
                /*
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp(1);
                    System.out.println(ts.toString());
                }*/
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("Failed to update the db");
            }
            finally {
                conn.close();
                conn=null;
            }


        }
        String url2 = "jdbc:sqlserver://localhost;databaseName=pzwpj_baza;";
        String login = "pzwpj_login";
        String password = "pzwpj_haslo";
        ConnectionPoolWithClosing connectionPool = BasicConnectionPool.create(url2, login, password);
        Connection c = connectionPool.getConnection();
        if (c != null) {

            System.out.println("Connected - connection pool");
            try {
                PreparedStatement statement = c.prepareStatement("select sysdatetime()");
                ResultSet rs = statement.executeQuery();
                System.out.println("Successfully executed query - connection pool");

                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp(1);
                    System.out.println(ts.toString());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("Failed to query the db - connection pool");
            }
            finally {
                connectionPool.releaseConnection(c);
                System.out.println("release connection - connection pool");
            }


        }

        var cc = connectionPool.getConnection();

        var preparedStatement = cc.prepareStatement("select * from pzwpj_schema.products where productName=?");
        preparedStatement.setString(1,"smartfon");
        var rs = preparedStatement.executeQuery();
        while (rs.next())
        {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            int category = rs.getInt(3);
            String unit = rs.getString(4);
            int quantity = rs.getInt(5);
            System.out.println("id" + Integer.toString(id) + ", name: "+name +", category id: " +Integer.toString(category) + ", unit: "+ unit+ ", quantity: "+ Integer.toString(quantity));


        }
        connectionPool.releaseConnection(cc);
        connectionPool.closeAllConnections();
        System.out.println("close all connections - connection pool");

        System.out.println("Test Connection Manager");
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        //var conn2 = manager.getConnection();
        var newAddressDAO = new AddressDAO();
        Address address = new Address(0);
        address.setAppartmentNumber(1);
        address.setAppartmentNumberAppendix("");
        address.setCity("Łódź");
        address.setCountry("Poland");
        address.setRegion("łódzkie");
        address.setBuildingNumber(15);
        address.setStreet("miedziana");
        address.setPostalCode("90-233");

        System.out.println("List of all addresses");
        LinkedList<Address> addresses = newAddressDAO.getAllAddresses();
        for(Address adr : addresses)
        {
            System.out.println(adr);
        }
        Address updated = addresses.getFirst();
        System.out.println("adr to update: "+updated);
        updated.setStreet("Mahakamska");
        newAddressDAO.update(updated);
        addresses = newAddressDAO.getAllAddresses();
        System.out.println("After update");
        for(Address adr : addresses)
        {
            System.out.println(adr);
        }


        System.out.println("End of list");
        /*
        try {
            newAddressDAO.create(address);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Failed to add new address using manager");
        }
        finally {
            //manager.releaseConnection(conn2);
        }
        */


    }
}
