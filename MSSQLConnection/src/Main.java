import ConnectionManagement.ConnectionPool.BasicConnectionPool;
import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPool.ConnectionPoolWithClosing;
import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import ConnectionManagement.Procedures.Functions;
import ConnectionManagement.Procedures.IFunctions;
import ConnectionManagement.Procedures.IProcedures;
import ConnectionManagement.Procedures.Procedures;
import model1.Address.AddressDAO;
import model1.Address.Address;
import model1.Address.IAddressDAO;
import model1.Category.Category;
import model1.Category.CategoryDAO;
import model1.Category.ICategoryDAO;
import model1.Customer.Customer;
import model1.Customer.CustomerDAO;
import model1.Customer.ICustomerDAO;
import model1.OrderPackage.Builder.DateType;
import model1.OrderPackage.Builder.IOrderBuilder;
import model1.OrderPackage.Builder.OrderBuilder;
import model1.OrderPackage.OrderAndOrderInterfaces.IOrderDAO;
import model1.OrderPackage.OrderAndOrderInterfaces.Order;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithAllMethods;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithGetters;
import model1.OrderPackage.OrderDAO;
import model1.Product.IProductDAO;
import model1.Product.Product;
import model1.Product.ProductDAO;
import model1.Supplier.Supplier;
import model1.Supplier.SupplierDAO;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.Calendar;
import java.util.LinkedList;

import static ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager.getInstance;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConnectionPool connectionPool = ConnectionPoolManager.getInstance();
        IAddressDAO addressDAO = new AddressDAO();
        IProductDAO productDAO = new ProductDAO();
        ICustomerDAO customerDAO = new CustomerDAO();
        IOrderDAO orderDAO = new OrderDAO();
        ICategoryDAO categoryDAO = new CategoryDAO();

        int quantity = 2;
        int quantity2 = 1;
        Product product = productDAO.getProductById(4);//iphone
        Product product2 = productDAO.getProductById(23);//smartwatch
        Address address = addressDAO.getAddressById(1);
        Customer customer = customerDAO.getCustomerById(1);
        OrderBuilder orderBuilder = Order.create();
        orderBuilder.addProduct(product, quantity);
        orderBuilder.addProduct(product2,quantity2);
        orderBuilder.setBuyer(customer);
        orderBuilder.setDeliveryAddress(address);
        orderBuilder.setDescription(Order.getNoDescription());
        var dd =Calendar.getInstance().getTime();
        Timestamp orderDate = new Timestamp(dd.getTime()  - 48*3600000 );
        Timestamp shipDate = new Timestamp(dd.getTime()  - 24*3600000 );
        Timestamp requireDate = new Timestamp(dd.getTime()  + 24*3600000 );
        orderBuilder.setDate( orderDate,DateType.order);
        orderBuilder.setDate( requireDate,DateType.require);
        orderBuilder.setDate(shipDate, DateType.ship);
        orderBuilder.setId(Order.getNoIdInDb());
        orderBuilder.setFreight(new BigDecimal(0.0));
        OrderWithAllMethods insertedOrder = orderBuilder.returnOrderWithAllMethods();
        if(insertedOrder != null)
        {
            System.out.println("Order builder returned not null");
        }
        var getOrderOnceAgain = orderBuilder.returnOrderWithGetters();
        if(getOrderOnceAgain == null)
        {
            System.out.println("Order builder returned null");
        }

        orderDAO.create(insertedOrder);
        System.out.println(insertedOrder);

        Runtime.getRuntime().addShutdownHook( new Thread(new Runnable(){

            @Override
            public void run() {

                try {
                    getInstance().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }));
    }

    public static void printAll(Iterable collection)
    {
        for (var o: collection
             ) {
            System.out.println(o);
        }
    }
}
