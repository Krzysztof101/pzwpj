package model1.OrderPackage;

import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.Address.AddressDAO;
import model1.Customer.Customer;
import model1.Customer.CustomerDAO;
import model1.OrderPackage.Builder.DateType;
import model1.OrderPackage.Builder.OrderBuilder;
import model1.OrderPackage.OrderAndOrderInterfaces.IOrderDAO;
import model1.OrderPackage.OrderAndOrderInterfaces.Order;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithAllMethods;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithGetters;
import model1.Product.Product;
import model1.Product.ProductDAO;
import model1.SQLErrorClasses.ErrorCodes;
import model1.SQLErrorClasses.SQLExceptionExt;
import model1.Address.*;

import java.math.BigDecimal;
import java.sql.*;

public class OrderDAO implements IOrderDAO {
    @Override
    public int create(OrderWithAllMethods order) throws SQLException {

        //pobierz obiekt Connection

        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();

        //zapisz poprzednie właściwości obiektu connection, by je później przywrócić
        boolean previousAutoCommit = conn.getAutoCommit();
        var tranIsol = conn.getTransactionIsolation();
        conn.setAutoCommit(false);

        //rozpocznij transakcję

        //utwórz string sql i prepared statement
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        String sql = "insert into pzwpj_schema.orders(customerId, orderDate, shipdate, requireDate, freight, description, addressId)" +
                " values (?, ?, ?, ?, ?, ?, ?);";
        String sql2 = "insert into pzwpj_schema.orderDetails(orderId,productId,quantity) values(?,?,?);";
        var productsStatement  =conn.prepareStatement(sql2, PreparedStatement.RETURN_GENERATED_KEYS);
        //ustaw parametr RETURN_GENERATED_KEYS, by móc pobrać automatycznie wygenerowany klucz
        int i=1;
        PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        //przekaż wartości do tabeli order
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
            //wykonaj insert na tabeli order
            retVal += statement.executeUpdate();

            if(retVal>0) {
                //pobierz wygenerowany klucz
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next())
                newOrderId = rs.getInt(1);
            }

        //przygotuj obiekt PreparedStatement do wpisywania wartości do tabeli order details
        productsStatement.setInt(1,newOrderId);
        for(int ii= 0 ; ii< order.size(); ii++)
        {
            //przekazuj do obiektu PreparedStatement dane nt. kolejnego zamawianego produktu
            productsStatement.setInt(2,order.getProduct(ii).getId());
            productsStatement.setInt(3, order.getQuantityOfProductAtPosition(ii));
            //wstaw dane nt. produktu
            retVal+=productsStatement.executeUpdate();
        }
        //zatwierdź wprowadzone w bazie zmiany
        conn.commit();
        }
        catch (SQLException e)
        {
            //w wypadku błędu cofnij zmiany
            conn.rollback();
            throw prepareInfoAboutError(e, ErrorCodes.INSERT_ORDER_ERROR);
        }
        finally {
            //przywróć obiek Connection do poprzedniego stanu i zwolnij zasoby

            productsStatement.close();
            statement.close();
            conn.setTransactionIsolation(tranIsol);
            conn.setAutoCommit(previousAutoCommit);
            manager.releaseConnection(conn);

        }
        return retVal;

    }
    @Override
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
    @Override
    public OrderWithGetters getOrderWithGetters(int id) throws SQLException {

        Connection conn = ConnectionPoolManager.getInstance().getConnection();
        return getOrderPriv(id );
    }
    @Override
    public OrderWithAllMethods getOrderWithAllMethods(int id) throws SQLException
    {
        Connection conn = ConnectionPoolManager.getInstance().getConnection();
        return getOrderPriv(id );
    }

    private int deletePriv(Order orderToDelete, Connection conn) throws SQLException {
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
        }
        return retVal;
    }

    private int createPriv(Order order, Connection conn) throws SQLException {
        //ConnectionPool manager = ConnectionPoolManager.getInstance();
        //Connection conn = manager.getConnection();
        //boolean previousAutoCommit = conn.getAutoCommit();
        //conn.setAutoCommit(false);
        //conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        String sql = "insert into pzwpj_schema.orders(customerId, orderDate, shipdate, requireDate, freight, description, addressId)" +
                " values (?, ?, ?, ?, ?, ?, ?);";
        String sql2 = "insert into pzwpj_schema.orderDetails(orderId,productId,quantity) values(?,?,?);";
        int prevOrderId= order.getId();
        var productsStatement  =conn.prepareStatement(sql2, PreparedStatement.RETURN_GENERATED_KEYS);
        int i=1;
        PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
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
                if (rs.next())
                    newOrderId = rs.getInt(1);


                productsStatement.setInt(1, newOrderId);
                for (int ii = 0; ii < order.size(); ii++) {

                    productsStatement.setInt(2, order.getProduct(ii).getId());
                    productsStatement.setInt(3, order.getQuantityOfProductAtPosition(ii));
                    retVal += productsStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e)
        {
            order.setId(prevOrderId);
            //conn.rollback();
            throw prepareInfoAboutError(e, ErrorCodes.INSERT_ORDER_ERROR);
        }
        finally {
            productsStatement.close();
            statement.close();
            //conn.setAutoCommit(previousAutoCommit);
            //manager.releaseConnection(conn);

        }
        return retVal;
    }

    @Override
    public int update(OrderWithGetters orderToUpdate1) throws SQLException {
        //pobierz obiekt Connection
        Order orderToUpdate = (Order) orderToUpdate1;
        Connection conn = ConnectionPoolManager.getInstance().getConnection();

        //zapisz aktualną wartośćautoCommit i transactionIsolation, by ją przywrócić po wykonaniu zapytania
        boolean prevAutoCommit = conn.getAutoCommit();
        var tranIsol = conn.getTransactionIsolation();
        int retVal=0;

        //rozpocznij transakcję
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        conn.setAutoCommit(false);
        try {
            //wykonaj pierwszą operację - delete
            int deletedRows = deletePriv(orderToUpdate, conn);
            if(deletedRows > 0)
            {
                //jeśli coś z bazy skasowano, to wstaw nowe wartości
                retVal =createPriv(orderToUpdate, conn);

                //zatwierdź wprowadzone zmiany
                conn.commit();

            }
            else {
                conn.rollback();
                //jeśli nic nie usunięto to wycofaj wszystkie zmiany
            }

        }
        catch (SQLException e)
        {
            //wycofaj wszystkie zmiany jeśli pojawił się błąd
            conn.rollback();
            throw prepareInfoAboutError(e, ErrorCodes.UPDATE_ORDER_ERROR);
        }
        finally {
            //zwolnienie zasobów
            conn.setAutoCommit(prevAutoCommit);
            conn.setTransactionIsolation(tranIsol);
            ConnectionPoolManager.getInstance().releaseConnection(conn);
        }
        return retVal;
    }




    private OrderWithAllMethods getOrderPriv(int id) throws SQLException {

        //utwórz budowniczego
        OrderBuilder builder = Order.create();

        //pobierz Connection
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "select * from pzwpj_schema.orders where orderId=?;";
        PreparedStatement statement = conn.prepareStatement(sql);

        //ustaw paramtry zapyanita
        statement.setInt(1,id);
        try {

            //wykonaj zapytanie
            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {

                //pobierz wartości i przekaż je do budowniczego
                int i=1;
                int orderId = rs.getInt(i++);
                int customerId = rs.getInt(i++);
                Timestamp ordDat = rs.getTimestamp(i++);
                Timestamp shpDat = rs.getTimestamp(i++);
                Timestamp reqDat = rs.getTimestamp(i++);

                builder.setId(orderId);
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


                //pobierz informacje nt. innych encji przy użyciu obiektów DAO
                int addressId = rs.getInt(i++);
                CustomerDAO customerDAO= new CustomerDAO();
                Customer customer = customerDAO.getCustomerById(customerId);
                AddressDAO addressDAO = new AddressDAO();
                Address deliveryAddr = addressDAO.getAddressById(addressId);


                //przekaż informacje te obiekty do budowniczego
                builder.setBuyer(customer);
                builder.setDeliveryAddress(deliveryAddr);


                //pobierz informacje nt. zamówionych produktów
                String sqlProducts = "Select * from pzwpj_schema.orderDetails where orderId = ?;";
                PreparedStatement productsStatement =  conn.prepareStatement(sqlProducts);
                productsStatement.setInt(1,id);
                try {
                    ResultSet productsList =  productsStatement.executeQuery();
                    ProductDAO productDAO = new ProductDAO();
                    while (productsList.next())
                    {
                        //pobieraj dane nt. kolejnego produktu z zamówienia - id i zamówioną ilość
                        int productId = productsList.getInt(2);
                        int quantity = productsList.getInt(3);

                        //pobierz z bazy dane nt. zamówionego produktu i przekaż go do budowniczego
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
            ConnectionPoolManager.getInstance().releaseConnection(conn);
        }
        //odzyskaj obiekt z budowniczego
        return builder.returnOrderWithAllMethods();
    }

    private SQLExceptionExt prepareInfoAboutError(SQLException e, int errorCode)
    {
        System.out.println(ErrorCodes.getMessage(errorCode));
        e.printStackTrace();
        return new SQLExceptionExt(e,errorCode);
    }
}


/*
        OrderBuilder builder = Order.create();
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "select * from pzwpj_schema.orders where orderId=?;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1,id);
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {
                int i=1;
                int orderId = rs.getInt(i++);
                int customerId = rs.getInt(i++);
                Timestamp ordDat = rs.getTimestamp(i++);
                Timestamp shpDat = rs.getTimestamp(i++);
                Timestamp reqDat = rs.getTimestamp(i++);

                builder.setId(orderId);
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

         */