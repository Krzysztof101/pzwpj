package model1.Category;

import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.SQLErrorClasses.ErrorCodes;
import model1.SQLErrorClasses.SQLExceptionExt;

import java.sql.*;
import java.util.LinkedList;

public class CategoryDAO {
    public static Category constructFromResultSet(ResultSet rs, int i) throws SQLException {
        int id = rs.getInt(i++);
        String name = rs.getString(i++);
        String description = rs.getString(i++);
        if(rs.wasNull())
        {
            description = Category.getNoDescription();
        }
        return new Category(id, name, description);
    }

    public int create(Category newCategory) throws SQLException, SQLExceptionExt {

        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "insert into pzwpj_schema.categories(categoryName, description) values(?, ?);";
        PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1,newCategory.getCategoryName());
        if(!newCategory.noDescription())
        {
            statement.setString(2,newCategory.getDescription());
        }
        else
        {
            statement.setNull(2, Types.NULL);
        }
        int retVal = 0;
        try
        {
            retVal = statement.executeUpdate();
            if(retVal>0)
            {
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next())
                {
                    int newId = rs.getInt(1);
                    newCategory.setId(newId);
                }
            }
        }
        catch(SQLException e)
        {
            throw new SQLExceptionExt(e, ErrorCodes.INSERT_CATEGORY_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }
    public int update(Category categoryToUpdate) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "update pzwpj_schema.categories set categoryName=?, description=? where categoryId = ?;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, categoryToUpdate.getCategoryName());
        if(!categoryToUpdate.noDescription())
        {
            statement.setString(2,categoryToUpdate.getDescription());
        }
        else
        {
            statement.setNull(2,Types.NULL);
        }
        statement.setInt(3,categoryToUpdate.getId());
        var retVal = 0;
        try {
            retVal = statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e,ErrorCodes.UPDATE_CATEGORY_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }
    public LinkedList<Category> getAllCategories() throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "Select * from pzwpj_schema.categories";
        PreparedStatement statement = conn.prepareStatement(sql);
        LinkedList<Category> categories = new LinkedList<>();
        try {
            ResultSet rs= statement.executeQuery();
            while (rs.next())
            {
                var id = rs.getInt("categoryId");
                var categoryName = rs.getString("categoryName");
                var categoryDescription = rs.getString("description");
                if(rs.wasNull())
                {
                    categoryDescription = Category.getNoDescription();
                }
                Category fetchedCategory = new Category(id,categoryName,categoryDescription);
                categories.add(fetchedCategory);
            }
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.SELECT_ALL_CATEGORY_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return categories;
    }
    public Category getCategoryById(int id) throws SQLException, SQLExceptionExt {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "Select * from pzwpj_schema.categories where categoryId = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        Category retVal = null;
        statement.setInt(1,id);
        try
        {
            //TODO check in addressDao if similar method is correct
            ResultSet rs= statement.executeQuery();
            if(rs.next())
            {
                var categoryName = rs.getString("categoryName");
                var categoryDescription = rs.getString("description");
                if(rs.wasNull())
                {
                    categoryDescription = Category.getNoDescription();
                }
                retVal = new Category(id,categoryName,categoryDescription);
            }

        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e,ErrorCodes.SELECT_SINGLE_CATEGORY_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }
    public int delete(Category categoryToDelete) throws SQLException {
        int id = categoryToDelete.getId();
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "DELETE from pzwpj_schema.categories where categoryId = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1,id);
        int retVal = 0;
        try {
            retVal = statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e,ErrorCodes.DELETE_CATEGORY_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;



    }

    private SQLExceptionExt prepareInfoAboutError(SQLException e, int errorCode)
    {
        System.out.println(ErrorCodes.getMessage(errorCode));
        e.printStackTrace();
        return new SQLExceptionExt(e,errorCode);
    }


}
