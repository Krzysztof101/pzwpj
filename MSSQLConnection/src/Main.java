import ConnectionManagement.ConnectionPool.BasicConnectionPool;
import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPool.ConnectionPoolWithClosing;
import ConnectionManagement.Procedures.Functions;
import ConnectionManagement.Procedures.IFunctions;
import ConnectionManagement.Procedures.IProcedures;
import ConnectionManagement.Procedures.Procedures;
import model1.Address.AddressDAO;
import model1.Address.Address;
import model1.Category.Category;
import model1.Category.CategoryDAO;
import model1.Customer.Customer;
import model1.Customer.CustomerDAO;
import model1.OrderPackage.OrderAndOrderInterfaces.Order;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithAllMethods;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithGetters;
import model1.OrderPackage.OrderDAO;
import model1.Product.Product;
import model1.Product.ProductDAO;
import model1.Supplier.Supplier;
import model1.Supplier.SupplierDAO;

import java.sql.*;
import java.time.Instant;
import java.util.Calendar;
import java.util.LinkedList;

import static ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager.getInstance;

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
        ConnectionPool manager = getInstance();
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
        System.out.println("Test addresses");
        Address address1 = new Address(0);
        address1.setPostalCode("00-001");
        address1.setStreet("pomidorowa");
        address1.setBuildingNumber(220);
        address1.setAppartmentNumber(24);
        address1.setCity("Chorzów");
        address1.setRegion("śląskie");
        address1.setCountry("Poland");
        AddressDAO addressDAO = new AddressDAO();
        addressDAO.create(address1);
        Address address2 = addressDAO.getAddressById(address1.getId());
        System.out.println("Test select: ");
        System.out.println("addr1: "+ address1);
        System.out.println("addr2: "+ address2);
        System.out.println("End test select");
        System.out.println("Test update");
        address1.setCity("Katowice");
        System.out.println("Before: "+address1);
        addressDAO.update(address1);
        System.out.println("After: ");
        Address address3 = addressDAO.getAddressById(address1.getId());
        System.out.println(address3);
        System.out.println("End test select");
        System.out.println("test delete");
        addressDAO.delete(address3);
        System.out.println("end test delete");
        System.out.println("EndTest");

        System.out.println("Test customer creation, single select and delete: ");
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getCustomerById(1);
        System.out.println(customer);
        Customer customer1 = new Customer(0,"AMD", customer.getAddress());
        customerDAO.create(customer1);
        Customer testSelect = customerDAO.getCustomerById(customer1.getId());
        System.out.println("compare created and selected: ");
        System.out.println(testSelect);
        System.out.println(customer1);
        System.out.println("end compare");
        customerDAO.delete(customer1);
        System.out.println("test done");

        System.out.println("Test category creation, single select and delete: ");
        CategoryDAO categoryDAO = new CategoryDAO();
        Category category = new Category(0,"sprzęt trenigowy", "");
        categoryDAO.create(category);
        category.setCategoryName("sprzęt do treningu");
        categoryDAO.update(category);
        Category category1 = categoryDAO.getCategoryById(category.getId());
        categoryDAO.delete(category1);
        System.out.println("compare created and selected: ");
        System.out.println(category1);
        System.out.println(category);
        System.out.println("End compare");
        System.out.println("end of test");

        System.out.println("Test product");
        Category category2 = categoryDAO.getCategoryById(1);
        Product product = new Product(0,category2,"playstation","sztuka", 345 );
        ProductDAO productDAO = new ProductDAO();
        System.out.println("Test create");
        //productDAO.create(product);
        System.out.println("created, test select");
        Product product1 = productDAO.getProductById(product.getId());
        System.out.println("Before: "+product);
        System.out.println("After: "+ product1);
        System.out.println("End test product");

        OrderDAO orderDAO = new OrderDAO();
        OrderWithAllMethods order = orderDAO.getOrderWithAllMethods(9);
        Order ord = (Order) order;
        if(order==null) {
            System.out.println("Order is null");
        }

        System.out.println("Order created: \n" + ord);
        /*
        System.out.println("test order update");
        Product prod = productDAO.getProductById(9);
        order.addProduct(prod, 5);
        System.out.println("BeforeOrderUpdate");
        orderDAO.update(order);
        System.out.println("AfterOrderUpdate");
         */
        System.out.println("end of order.toString()");

        System.out.println("test address - create address with not default country");
        //insert into pzwpj_schema.addresses (street,buildingNumber,appartmentNumberAppendix,city,country,postalCode) values ('lange',42,'a','Berlin','Germany','17-453');
        Address addressGermany = new Address();
        addressGermany.setStreet("lange");
        addressGermany.setBuildingNumber(72);
        addressGermany.setAppartmentNumberAppendix("b");
        addressGermany.setCity("Berlin");
        addressGermany.setCountry("Germany");
        addressGermany.setPostalCode("17-777");
        addressGermany.setRegion("Berlin");
        System.out.println("before insert "+addressGermany);
        System.out.println("insert");
        addressDAO.create(addressGermany);
        System.out.println("After insert");
        System.out.println(addressGermany);

        System.out.println("Test supplier dao");
        SupplierDAO supplierDAO = new SupplierDAO();
        Supplier supplier = new Supplier("Mleczarnia", addressDAO.getAddressById(2));
        System.out.println("newly created supplier: "+ supplier);
        supplierDAO.create(supplier);
        System.out.println("After: "+supplier);
        System.out.println("select:");
        Supplier supplier1 = supplierDAO.getSupplierById(supplier.getId());
        System.out.println("supplier from select: "+supplier1);
        supplier1.setName("Sad Jana Kowalskiego");
        supplierDAO.update(supplier1);
        System.out.println("End test supplier dao");


        System.out.println("Test select all");
        LinkedList<Address> allAddresses = addressDAO.getAllAddresses();
        printAll(allAddresses);
        LinkedList<Category> allCategories = categoryDAO.getAllCategories();
        printAll(allCategories);
        LinkedList<Customer> allCustomers = customerDAO.getAllCustomers();
        printAll(allCustomers);
        LinkedList<Product> allProducts = productDAO.getAllProducts();
        printAll(allProducts);
        LinkedList<Supplier> allSuppliers = supplierDAO.getAllSuppliers();
        printAll(allSuppliers);

        System.out.println("New features !!!");

        IFunctions functions = new Functions();
        LinkedList<Customer> customersInWarsaw = functions.getAllCustomersInCity("warszawa");
        System.out.println("test function customers in city");
        printAll(customersInWarsaw);
        System.out.println("Test function monthly sales");
        Category ccc = new Category(1,"assa","assdaad");
        var dd =Calendar.getInstance().getTime();
        Timestamp t = new Timestamp(dd.getTime());

        var prod = productDAO.getProductById(23);
        Date date = new Date(2021,5,5);
        Timestamp ts=new Timestamp(date.getTime());
        int sales = functions.getSalesOfProductInMonth(prod, t);
        System.out.println("Sales: "+Integer.toString(sales));

        IProcedures procedures = new Procedures();
        Category firstCategory = allCategories.getFirst();
        firstCategory.setCategoryName("updatedByProcedure");
        System.out.println("Test stored procedure");
        procedures.updateCategoryUsingProcedure(firstCategory);
        System.out.println("Test ended");
        LinkedList<Integer> ints = new LinkedList<>();
        for(int i = 1; i<=10; i++)
        {
            ints.add(i);
        }
        System.out.println("get products by list test");
        //LinkedList<Product> productsByArrayMethod = productDAO.getCollectionOfProducts(ints);

        //printAll(productsByArrayMethod);
        System.out.println("End test");

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
