package ConnectionManagement.Procedures;

import model1.Customer.Customer;
import model1.Product.Product;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

public interface IFunctions {
    LinkedList<Customer> getAllCustomersInCity(String city) throws SQLException;
    int getSalesOfProductInMonth(Product product, Timestamp date) throws SQLException;
}
