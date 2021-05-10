package ConnectionManagement.Procedures;

import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.Address.Address;
import model1.Address.AddressDAO;
import model1.Customer.Customer;
import model1.Product.Product;
import model1.SQLErrorClasses.ErrorCodes;

import java.sql.*;
import java.util.LinkedList;

public class Functions implements IFunctions{
    @Override
    public int getSalesOfProductInMonth(Product product, Timestamp date) throws SQLException {
        /*
        DECLARE @ret int;
        DECLARE @date2 Date = ?;
        DECLARE @prodId2 = ?;
        exec @ret = pzwpj_schema.getSaleCountInMonth @prodId = @prodId2, @date= @date2;
         */
        //"exec ? = pzwpj_schema.getSaleCountInMonth @prodId =?, @date =? ";
        Connection conn = ConnectionPoolManager.getInstance().getConnection();
        System.out.println(product);
        System.out.println("id: "+ Integer.toString(product.getId())+ " date: "+ date);
        String sql = "select Sum( pzwpj_schema.getSaleCountInMonth(?,?) )";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1,product.getId());
        statement.setTimestamp(2, date);
        int retVal = -1;
        try {

            ResultSet rs = statement.executeQuery();
                    if(rs.next())
                    {
                        retVal = rs.getInt(1);
                    }


        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.SELECT_FUNCTION_GET_PRODUCT_MONTHLY_SALES_ERROR);
        }
        finally {
            statement.close();
            ConnectionPoolManager.getInstance().releaseConnection(conn);
        }
        return retVal;

        /*
        Connection conn = ConnectionPoolManager.getInstance().getConnection();
        String sql = "Select pzwpj_schema.getSaleCountInMonth(?, ?) 'sold';";
        PreparedStatement statement = conn.prepareStatement(sql,Types.INTEGER);
        statement.setInt(1,product.getId());
        statement.setTimestamp(2, date);
        int retVal = -1;
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {
                retVal = rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.SELECT_FUNCTION_GET_PRODUCT_MONTHLY_SALES_ERROR);
        }
        finally {
            statement.close();
            ConnectionPoolManager.getInstance().releaseConnection(conn);
        }
        return retVal;
         */
    }
    @Override
    public LinkedList<Customer> getAllCustomersInCity(String city) throws SQLException {
        Connection conn = ConnectionPoolManager.getInstance().getConnection();
        String sql = "select * from pzwpj_schema.selectAllCustomersInCity(?);";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1,city);
        LinkedList<Customer> customersInCity = new LinkedList<>();
        try
        {
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                int cstId = rs.getInt("customerId");
                String cstName = rs.getString("companyName");
                Address address = AddressDAO.buildUsingResultSet(rs,4);
                Customer customer = new Customer(cstId,cstName,address);
                customersInCity.add(customer);
            }
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.SELECT_CUSTOMERS_IN_CITY_FUNCTION_ERROR);
        }
        finally {
            statement.close();
            ConnectionPoolManager.getInstance().releaseConnection(conn);
        }
        return  customersInCity;
    }


    private SQLException prepareInfoAboutError(SQLException e, int errorCode)
    {
        System.out.println(ErrorCodes.getMessage(errorCode));
        e.printStackTrace();
        SQLException ee = new SQLException(ErrorCodes.getMessage(errorCode) + e.getMessage(), e.getSQLState(), errorCode, e.getCause());
        return  ee;
    }
}
