package model1.Product;

import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.Category.Category;
import model1.Category.CategoryDAO;
import model1.SQLErrorClasses.ErrorCodes;
import model1.SQLErrorClasses.SQLExceptionExt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class ProductDAO {
    public LinkedList<Product> getAllProducts() throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "select * from pzwpj_schema.products join pzwpj_schema.categories on pzwpj_schema.products.categoryId = pzwpj_schema.categories.categoryId";
        PreparedStatement statement = conn.prepareStatement(sql);
        LinkedList<Product> products = new LinkedList<>();
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {

                int prodId = rs.getInt("productId");
                String prodName = rs.getString("productName");
                String unit = rs.getString("unit");
                int quantity = rs.getInt("quantity");
                Category category = CategoryDAO.constructFromResultSet(rs,6);
                Product product = new Product(prodId,category,prodName,unit,quantity);
                products.add(product);
            }
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.INSERT_PRODUCT_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return products;
    }

    public Product getProductById(int id) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "select * from pzwpj_schema.products join pzwpj_schema.categories on pzwpj_schema.products.categoryId = pzwpj_schema.categories.categoryId where pzwpj_schema.products.productId=?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1,id);
        Product product = null;
        try {
            ResultSet rs = statement.executeQuery();
            if(rs.next())
            {
                int prodId = rs.getInt("productId");
                String prodName = rs.getString("productName");
                String unit = rs.getString("unit");
                int quantity = rs.getInt("quantity");
                Category category = CategoryDAO.constructFromResultSet(rs,6);
                product = new Product(prodId,category,prodName,unit,quantity);
            }

        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.SELECT_SINGLE_PRODUCT_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return product;
    }
    public int create(Product newProduct) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "insert into pzwpj_schema.products (productName, categoryId, unit, quantity) values (?,?, ?, ?);";
        PreparedStatement statement = conn.prepareStatement(sql);
        int i=1;
        statement.setString(i++,newProduct.getProductName());
        statement.setInt(i++, newProduct.getCategory().getId());
        statement.setString(i++, newProduct.getUnit());
        statement.setInt(i++,newProduct.getQuantity());
        int retVal=0;
        try {
            retVal = statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.INSERT_PRODUCT_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;

    }

    public int delete(Product toDelete) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "delete from pzwpj_schema.products where productId =?;";
        PreparedStatement statement = conn.prepareStatement(sql);
        int retVal = 0;
        try {
            retVal = statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.DELETE_PRODUCT_ERROR);
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
