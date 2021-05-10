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


        int retVal = 0;
        try(CallableStatement statement = conn.prepareCall(sql);)
        {

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
            statement.registerOutParameter(4, Types.INTEGER);
            statement.execute();
            retVal = statement.getInt(4);
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.UPDATE_CATEGORY_PROCEDURE_ERROR);
        }
        finally {
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
