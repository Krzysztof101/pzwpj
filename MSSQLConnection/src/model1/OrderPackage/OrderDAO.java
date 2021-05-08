package model1.OrderPackage;

import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.Address.AddressDAO;
import model1.Customer.Customer;
import model1.Customer.CustomerDAO;
import model1.OrderPackage.Builder.DateType;
import model1.OrderPackage.Builder.OrderBuilder;
import model1.OrderPackage.OrderAndOrderInterfaces.Order;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithGetters;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithSetters;
import model1.Product.Product;
import model1.Product.ProductDAO;
import model1.SQLErrorClasses.ErrorCodes;
import model1.SQLErrorClasses.SQLExceptionExt;
import model1.Address.*;

import java.math.BigDecimal;
import java.sql.*;

public class OrderDAO {
    public int create(OrderWithGetters order) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        boolean previousAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        String sql = "insert into pzwpj_schema.orders(customerId, orderDate, shipdate, requireDate, freight, description, addressId)" +
                " values (?, ?, ?, ?, ?, ?, ?);";
        String sql2 = "insert into pzwpj_schema.orderDetails(orderId,productId,quantity) values(?,?,?);";
        var productsStatement  =conn.prepareStatement(sql2);
        int i=1;
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(i++,order.getBuyer().getId() );
        statement.setTimestamp(i++, order.getOrderDate());
        statement.setTimestamp(i++, order.getShipDate());
        statement.setTimestamp(i++, order.getRequireDate());
        statement.setBigDecimal(i++, order.getFreight());
        if(!Order.noDescription(order.getDesccription()))
        {
            statement.setString(i++, order.getDesccription());
        }
        else
        {
            statement.setNull(i++, Types.NULL);
        }
        statement.setInt(i++, order.getDeliveryAddress().getId());
        int retVal=0;
        int newOrderId = 0;
        try {
            retVal += statement.executeUpdate();

            if(retVal>0) {
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next())
                newOrderId = rs.getInt(1);
            }


        productsStatement.setInt(1,newOrderId);
        for(int ii= 0 ; ii< order.size(); ii++)
        {

            productsStatement.setInt(2,order.getProduct(ii).getId());
            productsStatement.setInt(3, order.getQuantityOfProductAtPosition(ii));
            retVal+=productsStatement.executeUpdate();
        }
        conn.commit();
        }
        catch (SQLException e)
        {
            conn.rollback();
            throw prepareInfoAboutError(e, ErrorCodes.INSERT_ORDER_ERROR);
        }
        finally {
            productsStatement.close();
            statement.close();
            conn.setAutoCommit(previousAutoCommit);
            manager.releaseConnection(conn);

        }
        return retVal;

    }

    public int delete(Order orderToDelete) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "delete from pzwpj_schema.orders where orderId=?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, orderToDelete.getId());
        int retVal=0;
        try {
            retVal = statement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.DELETE_ORDER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }

    public OrderWithGetters getOrder(int id) throws SQLException {
        OrderBuilder builder = Order.create();
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "select pzwpj_schema.orders where orderId=?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1,id);
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {
                int i=1;
                int customerId = rs.getInt(i++);
                Timestamp ordDat = rs.getTimestamp(i++);
                Timestamp shpDat = rs.getTimestamp(i++);
                Timestamp reqDat = rs.getTimestamp(i++);

                builder.setDate(ordDat, DateType.order);
                builder.setDate(shpDat, DateType.ship);
                builder.setDate(reqDat, DateType.require);


                BigDecimal freight = rs.getBigDecimal(i++);
                String desc = rs.getString(i++);
                if(rs.wasNull())
                {
                    desc = Order.getNoDescription();
                }

                builder.setFreight(freight);
                builder.setDescription( desc);

                int addressId = rs.getInt(i++);
                CustomerDAO customerDAO= new CustomerDAO();
                Customer customer = customerDAO.getCustomerById(customerId);
                AddressDAO addressDAO = new AddressDAO();
                Address deliveryAddr = addressDAO.getAddressById(addressId);

                builder.setBuyer(customer);
                builder.setDeliveryAddress(deliveryAddr);

                String sqlProducts = "Select * from pzwpj_schema.orderDetails where orderId = ?;";
                PreparedStatement productsStatement =  conn.prepareStatement(sqlProducts);
                productsStatement.setInt(1,id);
                try {
                    ResultSet productsList =  productsStatement.executeQuery();
                    ProductDAO productDAO = new ProductDAO();
                    while (productsList.next())
                    {
                        int productId = productsList.getInt(2);
                        int quantity = productsList.getInt(3);
                        Product product = productDAO.getProductById(productId);
                        builder.addProduct(product,quantity);
                    }
                }
                catch (SQLException e)
                {

                    throw e;
                }
                finally {
                    productsStatement.close();
                }


            }
        } catch (SQLException ee) {

            prepareInfoAboutError(ee, ErrorCodes.SELECT_SINGLE_ORDER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return builder.returnOrderWithAllMethods();
    }

    private SQLExceptionExt prepareInfoAboutError(SQLException e, int errorCode)
    {
        System.out.println(ErrorCodes.getMessage(errorCode));
        e.printStackTrace();
        return new SQLExceptionExt(e,errorCode);
    }
}
