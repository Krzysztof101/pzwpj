package model1.Customer;

import java.sql.SQLException;
import java.util.LinkedList;

public interface ICustomerDAO {
    int create(Customer newCustomer) throws SQLException;
    Customer getCustomerById(int id) throws SQLException;
    LinkedList<Customer> getAllCustomers() throws SQLException;
    int update(Customer customerToUpdate) throws SQLException;
    int delete(Customer customerToDelete) throws SQLException;
}
