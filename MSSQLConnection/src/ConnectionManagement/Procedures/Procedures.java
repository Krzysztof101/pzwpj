package ConnectionManagement.Procedures;

import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.Category.Category;
import model1.SQLErrorClasses.ErrorCodes;

import java.sql.*;

public class Procedures implements IProcedures{
    @Override
    public int updateCategoryUsingProcedure(Category toUpdate) throws SQLException {
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "exec pzwpj_schema.updateCategory ?, ?, ?, ?;";
                                                //ostatni z argumentów procedury jest argumentem wyjściowym

        int retVal = 0;

        //przygotuj CallableStatement
        //w przypadku wywołąnia procedury należy użyć CallableStatement

        try(CallableStatement statement = conn.prepareCall(sql);)
        {
            //przekaż wartości do parametrów callableStatement
            statement.setInt(1, toUpdate.getId());
            statement.setString(2, toUpdate.getCategoryName());
            if(toUpdate.noDescription())
            {
                statement.setNull(3, Types.NULL);
            }
            else
            {
                statement.setString(3, toUpdate.getDescription());
            }
            //zarejestruj parametr wyjściowy
            statement.registerOutParameter(4, Types.INTEGER);
            //wykonaj procedurę
            statement.execute();
            //odzyskaj dane z procedury
            retVal = statement.getInt(4);


            //przy wykorzystaniu takiej konstrukcji nie trzeba zamykać statement metodą close()
            //statement.close();
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.UPDATE_CATEGORY_PROCEDURE_ERROR);
        }
        finally {
            //zwolnij zasoby
            ConnectionPoolManager.getInstance().releaseConnection(conn);
        }
        return retVal;
    }

    private SQLException prepareInfoAboutError(SQLException e, int errorCode)
    {
        System.out.println(ErrorCodes.getMessage(errorCode));
        e.printStackTrace();
        SQLException ee = new SQLException(ErrorCodes.getMessage(errorCode) + e.getMessage(), e.getSQLState(), errorCode, e.getCause());
        return  ee;
    }
}
