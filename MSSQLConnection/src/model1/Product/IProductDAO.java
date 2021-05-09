package model1.Product;

import java.sql.SQLException;
import java.util.LinkedList;

public interface IProductDAO {
    int create(Product newProduct) throws SQLException;
    LinkedList<Product> getAllProducts() throws SQLException;
    Product getProductById(int id) throws SQLException;
    int delete(Product toDelete) throws SQLException;
    int update(Product productToUpdate) throws SQLException;
}
