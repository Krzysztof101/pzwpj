package model1.Address;

import java.sql.SQLException;
import java.util.LinkedList;

public interface IAddressDAO {
    int create(Address newAddress) throws SQLException;
    LinkedList<Address> getAllAddresses() throws SQLException;
    Address getAddressById(int id) throws SQLException;
    int update(Address addressToUpdate) throws SQLException;
    int delete(Address addressToDelete) throws SQLException;
}
