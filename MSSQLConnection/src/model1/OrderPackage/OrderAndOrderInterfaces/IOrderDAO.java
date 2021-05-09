package model1.OrderPackage.OrderAndOrderInterfaces;

import java.sql.SQLException;

public interface IOrderDAO {
    int create(OrderWithAllMethods order) throws SQLException;
    int delete(Order orderToDelete) throws SQLException;
    OrderWithGetters getOrderWithGetters(int id) throws SQLException;
    OrderWithAllMethods getOrderWithAllMethods(int id) throws SQLException;
    int update(OrderWithGetters orderToUpdate1) throws SQLException;
}
