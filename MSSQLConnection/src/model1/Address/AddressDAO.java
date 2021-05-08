package model1.Address;

import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.SQLErrorClasses.ErrorCodes;

import java.sql.*;
import java.util.LinkedList;

public class AddressDAO {
    public int create(Address newAddress) throws SQLException {
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "insert into pzwpj_schema.addresses(street, buildingNumber, appartmentNumber, appartmentNumberAppendix, city, country, postalCode, region)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int i=1;
        preparedStatement.setString(i++, newAddress.getStreet());
        preparedStatement.setInt(i++, newAddress.getBuildingNumber());
        if(!newAddress.noAppartmentNumber())
        {
            preparedStatement.setInt(i++, newAddress.getAppartmentNumber());
        }
        else
        {
            preparedStatement.setNull(i++, Types.NULL);
        }

        if(!newAddress.getAppartmentNumberAppendix().equals(""))
        {
            preparedStatement.setString(i++,newAddress.getAppartmentNumberAppendix());
        }else
        {
            preparedStatement.setNull(i++,Types.NULL);
        }
        preparedStatement.setString(i++, newAddress.getCity());
        preparedStatement.setString(i++, newAddress.getCountry());
        preparedStatement.setString(i++, newAddress.getPostalCode());

        if(!newAddress.getRegion().equals(""))
        { preparedStatement.setString(i++, newAddress.getRegion()); }
        else
        {
            preparedStatement.setNull(i++, Types.NULL);
        }
        /*
        preparedStatement.setString("street", newAddress.getStreet());
        preparedStatement.setInt("buildingNumber", newAddress.getBuildingNumber());
        preparedStatement.setInt("appartmentNumber", newAddress.getAppartmentNumber());
        if(!newAddress.getAppartmentNumberAppendix().equals(""))
        {
            preparedStatement.setString("appartmentNumberAppendix", newAddress.getAppartmentNumberAppendix());
        }else
        {
            preparedStatement.setNULL("appartmentNumberAppendix");
        }
        preparedStatement.setString("city", newAddress.getCity());
        preparedStatement.setString("postalCode", newAddress.getPostalCode());
        if(!newAddress.getRegion().equals(""))
        { preparedStatement.setString("region", newAddress.getRegion()); }
        else
        {
            preparedStatement.setNULL("region");
        }

         */
        int updatedRowsNumber= 0;
        try {
            updatedRowsNumber = preparedStatement.executeUpdate();
        }catch (SQLException e)
        {
            System.out.println("Error while inserting address to db");
            throw e;
        }
        finally {
            preparedStatement.close();
            manager.releaseConnection(conn);
        }

        return updatedRowsNumber;
    }

    private Address readSingleAddressFromResultSet(ResultSet rs) throws SQLException {
        int i=1;
        int id = rs.getInt("addressId");
        String street = rs.getString("street");
        int buildingNumber = rs.getInt("buildingNumber");
        int appartmentNumber = rs.getInt("appartmentNumber");
        if(rs.wasNull())
        {
            appartmentNumber = Address.getNoAppartmentNumber();
        }
        String appNumAppendix = rs.getString("appartmentNumberAppendix");
        if(rs.wasNull())
        {
            appNumAppendix = Address.getNoAppartmentNumberAppendix();
        }
        String city = rs.getString("city");
        String country = rs.getString("country");
        String postalCode = rs.getString("postalCode");
        String region = rs.getString("region");
        if(rs.wasNull())
        {
            region =Address.getNoRegion();
        }

        Address fetchedAddress = new Address(id);
        fetchedAddress.setStreet(street);
        fetchedAddress.setBuildingNumber(buildingNumber);
        fetchedAddress.setAppartmentNumber(appartmentNumber);
        fetchedAddress.setAppartmentNumberAppendix(appNumAppendix);
        fetchedAddress.setCity(city);
        fetchedAddress.setCountry(country);
        fetchedAddress.setPostalCode(postalCode);
        fetchedAddress.setRegion(region);
        return fetchedAddress;
    }
    public LinkedList<Address> getAllAddresses() throws SQLException {
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "Select * from pzwpj_schema.addresses;";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        LinkedList<Address> addresses = new LinkedList<>();
        try{
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next())
        {
            Address fetchedAddress = readSingleAddressFromResultSet(rs);
            addresses.add(fetchedAddress);
        }

        System.out.println("Retrieved all addresses - statement closed");

        }

        catch (SQLException e)
        {


            System.out.println("Failed while reading from db");
            e.printStackTrace();
            throw e;
        }
        finally {
            preparedStatement.close();
            manager.releaseConnection(conn);
        }
        return addresses;
    }


    public Address getAddressById(int id) throws SQLException {
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "Select * from pzwpj_schema.addresses where addressId=?;";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        Address fetchedAddress = null;
        try {
            var rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                fetchedAddress = readSingleAddressFromResultSet(rs);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error while reading single address from db");
            e.printStackTrace();
            throw e;
        }
        finally {
            preparedStatement.close();
            manager.releaseConnection(conn);
        }
        return fetchedAddress;
    }

    public int update(Address addressToUpdate) throws SQLException {
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "UPDATE pzwpj_schema.addresses set street = ?, buildingNumber = ?, appartmentNumber = ?, appartmentNumberAppendix = ?,   " +
                "  city = ? , country = ? , postalCode = ? , region = ? where addressId = ? ";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int i=1;
        preparedStatement.setString(i++, addressToUpdate.getStreet());
        preparedStatement.setInt(i++, addressToUpdate.getBuildingNumber());
        if(!addressToUpdate.noAppartmentNumber())
        {
            preparedStatement.setInt(i++, addressToUpdate.getBuildingNumber());
        }
        else
        {
            preparedStatement.setNull(i++, Types.NULL);
        }
        if(!addressToUpdate.noAppartmentNumberAppendix())
        {
            preparedStatement.setString(i++, addressToUpdate.getAppartmentNumberAppendix());
        }
        else
        {
            preparedStatement.setNull(i++,Types.NULL);
        }
        preparedStatement.setString(i++, addressToUpdate.getCity());
        preparedStatement.setString(i++, addressToUpdate.getCountry());
        preparedStatement.setString(i++, addressToUpdate.getPostalCode());
        if(!addressToUpdate.noRegion())
        {
            preparedStatement.setString(i++, addressToUpdate.getCountry());
        }
        else
        {
            preparedStatement.setNull(i++,Types.NULL);
        }
        preparedStatement.setInt(i++, addressToUpdate.getId());
        int retVal = 0;
        try {
            retVal = preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.UPDATE_ADDRESS_ERROR);
        }
        finally {
            preparedStatement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }

    public int delete(Address addressToDelete) throws SQLException {
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();
        String sql = "Delete from pzwpj_schema.addresses where addressId=?;";
        int retVal = 0;
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1,addressToDelete.getId());
        try {
            retVal = preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            var ee = prepareInfoAboutError(e,ErrorCodes.DELETE_ADDRESS_ERROR);
            throw ee;
        }
        finally {
           preparedStatement.close();
           manager.releaseConnection(conn);

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
    public static Address buildUsingResultSet(ResultSet rs, int adrItr) throws SQLException {
        int addressId = rs.getInt(adrItr++);
        String street = rs.getString(adrItr++);
        int building = rs.getInt(adrItr++);
        int appartment = rs.getInt(adrItr++);
        if(rs.wasNull())
        {
            appartment = Address.getNoAppartmentNumber();
        }
        String appendix = rs.getString(adrItr++);
        if(rs.wasNull())
        {
            appendix = Address.getNoAppartmentNumberAppendix();
        }
        String city = rs.getString(adrItr++);
        String country = rs.getString(adrItr++);
        String postalCode = rs.getString(adrItr++);
        String region = rs.getString(addressId++);
        if(rs.wasNull())
        {
            region = Address.getNoRegion();
        }
        Address address = new Address(addressId);
        address.setStreet(street);
        address.setRegion(region);
        address.setCountry(country);
        address.setCity(city);
        address.setBuildingNumber(building);
        address.setAppartmentNumber(appartment);
        address.setAppartmentNumberAppendix(appendix);
        address.setPostalCode(postalCode);
        return address;
    }

}
