package ConnectionManagement.Procedures;

import model1.Category.Category;

import java.sql.SQLException;

public interface IProcedures {
    int updateCategoryUsingProcedure(Category toUpdate) throws SQLException;
}
