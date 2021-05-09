package model1.Supplier;

import ConnectionManagement.ConnectionPool.ConnectionPool;
import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.Address.Address;
import model1.Address.AddressDAO;
import model1.Category.Category;
import model1.SQLErrorClasses.ErrorCodes;
import model1.SQLErrorClasses.SQLExceptionExt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class SupplierDAO {
    private final static int ROWS_AFFECTED=0;
    public int create(Supplier newSupplier) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "insert into pzwpj_schema.suppliers(companyName, addressId) values(?, ?);";
        PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, newSupplier.getCompanyName());
        statement.setInt(2, newSupplier.getAddress().getId());
        int retVal = ROWS_AFFECTED;
        try {
            retVal = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next())
            {
                int newId = rs.getInt(1);
                newSupplier.setId(newId);
            }
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.INSERT_SUPPLIER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }
    public int update(Supplier supplierToUpdate) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "update pzwpj_schema.suppliers set companyName=?, addressID=? where supplierId = ?;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, supplierToUpdate.getCompanyName());
        statement.setInt(2, supplierToUpdate.getAddress().getId());
        statement.setInt(3, supplierToUpdate.getId());
        int retVal = ROWS_AFFECTED;
        try {
            retVal = statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.UPDATE_SUPPLIER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }

    public LinkedList<Supplier> getAllSuppliers() throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "Select * from pzwpj_schema.suppliers join pzwpj_schema.addresses on pzwpj_schema.addresses.addressId = pzwpj_schema.suppliers.addressID;";
        PreparedStatement statement = conn.prepareStatement(sql);
        LinkedList<Supplier> suppliers = new LinkedList<>();

        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                Address compAddr = AddressDAO.buildUsingResultSet(rs, 4);

                int id = rs.getInt("supplierId");
                String cmpName = rs.getString("companyName");
                Supplier s = new Supplier(id, cmpName, compAddr);
                suppliers.add(s);
            }

        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.SELECT_ALL_SUPPLIER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return suppliers;
    }

    public Supplier getSupplierById(int id) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "Select * from pzwpj_schema.suppliers join pzwpj_schema.addresses on pzwpj_schema.addresses.addressId = pzwpj_schema.suppliers.addressID where pzwpj_schema.suppliers.supplierId=?;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, id);
        Supplier supplier = null;
        try {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Address compAddr = AddressDAO.buildUsingResultSet(rs, 4);

                int idd = rs.getInt("supplierId");
                String cmpName = rs.getString("companyName");
                supplier = new Supplier(idd, cmpName, compAddr);
            }
        }
        catch(SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.SELECT_SINGLE_SUPPLIER_ERROR);
        }
        finally {
            statement.close();
            manager.releaseConnection(conn);
        }
        return supplier;
    }

    public int delete(Supplier supplierToDelete) throws SQLException {
        ConnectionPool manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "delete from pzwpj_schema.suppliers where pzwpj_schema.supplierId=?;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, supplierToDelete.getId());
        int retVal=0;
        try {
            retVal = statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.DELETE_SUPPLIER_ERROR);
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
