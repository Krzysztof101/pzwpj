package model1.Customer;

import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.SQLErrorClasses.ErrorCodes;
import model1.Address.*;
import model1.SQLErrorClasses.SQLExceptionExt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class CustomerDAO implements ICustomerDAO{
    @Override
    public int create(Customer newCustomer) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "insert into pzwpj_schema.customers (companyName, addressId) values(?,?);";
        PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1,newCustomer.getCompanyName());
        statement.setInt(2,newCustomer.getAddress().getId());
        int retval=0;
        try {


            retval = statement.executeUpdate();

            //if(retval > 0)
            //{
                ResultSet rs = statement.getGeneratedKeys();

                if(rs.next())
                {
                    int newId = rs.getInt(1);
                    newCustomer.setId(newId);
                }

            //}
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.INSERT_CUSTOMER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retval;

    }
    @Override
    public Customer getCustomerById(int id) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "select * from pzwpj_schema.customers join pzwpj_schema.addresses on pzwpj_schema.customers.addressId = pzwpj_schema.addresses.addressId where customerId=?;";
        Customer customer = null;

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1,id);
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {

                int cstId = rs.getInt("customerId");
                String cstName = rs.getString("companyName");
                Address address = AddressDAO.buildUsingResultSet(rs,4);
                customer = new Customer(cstId,cstName,address);
            }

        } catch (SQLException sqlException) {
            throw prepareInfoAboutError(sqlException, ErrorCodes.SELECT_SINGLE_CUSTOMER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return customer;
    }
    @Override
    public LinkedList<Customer> getAllCustomers() throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "select* from pzwpj_schema.customers join pzwpj_schema.addresses on pzwpj_schema.customers.addressId = pzwpj_schema.addresses.addressId;";
        LinkedList<Customer> customers =new LinkedList<>();
        PreparedStatement statement = conn.prepareStatement(sql);
        try {
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                int cstId = rs.getInt("customerId");
                String cstName = rs.getString("companyName");
                Address address = AddressDAO.buildUsingResultSet(rs,4);
                Customer customer = new Customer(cstId,cstName,address);
                customers.add(customer);
            }

        } catch (SQLException sqlException) {
            throw prepareInfoAboutError(sqlException, ErrorCodes.SELECT_ALL_CUSTOMER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return customers;
    }

    @Override
    public int update(Customer customerToUpdate) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "update pzwpj_schema.customers set companyName=?, addressId=? where customerId=?;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, customerToUpdate.getCompanyName());
        statement.setInt(2, customerToUpdate.getAddress().getId());
        statement.setInt(3, customerToUpdate.getId());
        int retVal=0;
        try {
            retVal = statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e,ErrorCodes.UPDATE_CUSTOMER_ERROR);

        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }


    @Override
    public int delete(Customer customerToDelete) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "delete from pzwpj_schema.customers where customerId=?;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, customerToDelete.getId());
        int retVal =0;
        try {
            int rowsCreated = statement.executeUpdate();
        }
        catch (SQLException e)
        {
            prepareInfoAboutError(e, ErrorCodes.DELETE_CUSTOMER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }



    private SQLExceptionExt prepareInfoAboutError(SQLException e, int errorCode)
    {
        System.out.println(ErrorCodes.getMessage(errorCode));
        e.printStackTrace();
        return new SQLExceptionExt(e,errorCode);
    }
}
