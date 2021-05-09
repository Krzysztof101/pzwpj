package model1.Category;

import java.sql.SQLException;
import java.util.LinkedList;

public interface ICategoryDAO {
    int create(Category newCategory) throws SQLException;
    int update(Category categoryToUpdate) throws SQLException;
    LinkedList<Category> getAllCategories() throws SQLException;
    Category getCategoryById(int id) throws SQLException;
    int delete(Category categoryToDelete) throws SQLException;
}
