package model1.Address;

import ConnectionManagement.ConnectionPoolManager.ConnectionPoolManager;
import model1.SQLErrorClasses.ErrorCodes;

import java.sql.*;
import java.util.LinkedList;

public class AddressDAO implements IAddressDAO{
    @Override
    public int create(Address newAddress) throws SQLException {

        //pobierz połączenie

        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();

        //jdbc nie dostarcza żadnych mechanizmów do ustawiania wartości domyślnych
        //przygotuj string z zdaniem sql
        //kolumny w tabeli addresses: (street, buildingNumber, appartmentNumber, appartmentNumberAppendix, city, country, postalCode, region)
        String sql = prepareSql(newAddress);


        PreparedStatement preparedStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        //ustawienie parametru RETURN_GENERATED_KEYS po to, by móc odzyskać klucz, który został wygenerowany automatycznie przez DBMS


        //ustaw kolejne pola w zapytaniu
        int i=1;
        preparedStatement.setString(i++, newAddress.getStreet());
        preparedStatement.setInt(i++, newAddress.getBuildingNumber());

        //jeśli któreś pole ma być nullem to trzeba ustawić w odpowienim parametrze zapytania Types.NULL
        //jeśli się tago nie zrobi, to w czasie zapytania pojawi się błąd
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

        //jeśli kraj nie ma mieć domyślnej wartość, to ustaw odpowiedni parametr zapytania
        if(!newAddress.getCountryDefault()) {
            preparedStatement.setString(i++, newAddress.getCountry());
        }


        preparedStatement.setString(i++, newAddress.getPostalCode());

        if(!newAddress.getRegion().equals(""))
        { preparedStatement.setString(i++, newAddress.getRegion()); }
        else
        {
            preparedStatement.setNull(i++, Types.NULL);
        }


        //koniec ustawiania parametrów, próba wykonania zapytania

        int updatedRowsNumber= 0;
        try {
            updatedRowsNumber = preparedStatement.executeUpdate();


            //pobranie wygenerowanego automatycznie klucza
            ResultSet rs =preparedStatement.getGeneratedKeys();
            if(rs.next())
            {
                int newId = rs.getInt(1);
                newAddress.setId(newId);
            }

        }catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.INSERT_ADDRESS_ERROR);
        }
        finally {

            //zwolnienie zasobów
            preparedStatement.close();
            manager.releaseConnection(conn);
        }

        return updatedRowsNumber;
    }
    private String prepareSql(Address insertedAddress)
    {

        //kolumny w tabeli addresses:
        // (street, buildingNumber, appartmentNumber, appartmentNumberAppendix, city, country, postalCode, region)
        //jeśli chcemy, pole country miało domyślną wartość, to musimy pominąć je stringu zawierającym operację insert,
        //musimy również zmniejszyć o 1 ilość znaków zapytania w values

        if(insertedAddress.getCountryDefault())
        {
            return  "insert into pzwpj_schema.addresses(street, buildingNumber, appartmentNumber, appartmentNumberAppendix, city, postalCode, region)" +
                    "values (?, ?, ?, ?, ?, ?, ?);";
        }

        return  "insert into pzwpj_schema.addresses(street, buildingNumber, appartmentNumber, appartmentNumberAppendix, city, country, postalCode, region)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?);";
    }


    private Address readSingleAddressFromResultSet(ResultSet rs) throws SQLException {

        //pobierz kolejne wartości i przypisz je do zmiennych, by następnie ustawić odpowiednie wartości w obiekcie klasy Address
        //rekomendowana praktyka - pobieranie wartości przy pomocy metod pobierających wartości na podstawie nazwy ich kolumny
        int id = rs.getInt("addressId");
        String street = rs.getString("street");
        int buildingNumber = rs.getInt("buildingNumber");

        //przypadek wartości, która mogła być nullem
        //metoda getInt(String columnLabel) zwraca 0, gdy pobrano z bazy danych wartość, która była null-em
        int appartmentNumber = rs.getInt("appartmentNumber");

        //bezpośrednio po przypisaniu można sprawdzić czy wartość pobrana z bazy była null-em przy pomocy metody wasNull()
        if(rs.wasNull())
        {
            appartmentNumber = Address.getNoAppartmentNumber();
        }

        //jeśli wartość pobrana z bazy jest null-em getString(String columnLabel) zwróci pusty String
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

        //ustaw wszystkie pola w nowym adresie
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
    @Override
    public LinkedList<Address> getAllAddresses() throws SQLException {

        //pobierz obiekt connection z puli połączeń
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();

        //przygotuj obiekt PreparedStatement
        String sql = "Select * from pzwpj_schema.addresses;";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        LinkedList<Address> addresses = new LinkedList<>();
        try{
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next())
        {
            //przejdź w pętli przez wszystkie wiersze z tabeli i pobierz dane nt. wszystkich adresów
            Address fetchedAddress = readSingleAddressFromResultSet(rs);
            addresses.add(fetchedAddress);
        }

        }

        catch (SQLException e)
        {


            throw prepareInfoAboutError(e, ErrorCodes.SELECT_ALL_ADDRESS_ERROR);
        }
        finally {
            //zwolnienie zasobów
            preparedStatement.close();
            manager.releaseConnection(conn);
        }
        return addresses;
    }

    @Override
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
            throw prepareInfoAboutError(e, ErrorCodes.SELECT_SINGLE_ADDRESS_ERROR);

        }
        finally {
            preparedStatement.close();
            manager.releaseConnection(conn);
        }
        return fetchedAddress;
    }
    @Override
    public int update(Address addressToUpdate) throws SQLException {

        //pobierz obiekt connection
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();

        //przygotuj obiekt PreparedStatement
        String sql = "UPDATE pzwpj_schema.addresses set street = ?, buildingNumber = ?, appartmentNumber = ?, appartmentNumberAppendix = ?,   " +
                "  city = ? , country = ? , postalCode = ? , region = ? where addressId = ? ";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        //ustaw kolejne wartości w paramtrach obiektu PreparedStatement
        int i=1;
        preparedStatement.setString(i++, addressToUpdate.getStreet());
        preparedStatement.setInt(i++, addressToUpdate.getBuildingNumber());

        //w zależności od tego, czy dana wartość ma być null-em bazie danych ustaw parametr w odpowieni sposób
        if(!addressToUpdate.noAppartmentNumber())
        {
            preparedStatement.setInt(i++, addressToUpdate.getAppartmentNumber());
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
            preparedStatement.setString(i++, addressToUpdate.getRegion());
        }
        else
        {
            preparedStatement.setNull(i++,Types.NULL);
        }
        preparedStatement.setInt(i++, addressToUpdate.getId());


        int retVal = 0;
        try {
            retVal = preparedStatement.executeUpdate();
            //retVal - ilość wiersz zmienionych przez executeUpdate()
        }
        catch (SQLException e)
        {
            throw prepareInfoAboutError(e, ErrorCodes.UPDATE_ADDRESS_ERROR);
        }
        finally {

            //zwolnienie zasobów
            preparedStatement.close();
            manager.releaseConnection(conn);
        }
        return retVal;
    }
    @Override
    public int delete(Address addressToDelete) throws SQLException {

        //pobierz obiekt connection
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        Connection conn = manager.getConnection();

        //przygotuj obiekt PreparedStatement
        String sql = "Delete from pzwpj_schema.addresses where addressId=?;";
        int retVal = 0;
        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        //ustaw parametry
        preparedStatement.setInt(1,addressToDelete.getId());

        try {
            //wykonaj polecenie sql
            retVal = preparedStatement.executeUpdate();
            //retVal - ilość wiersz zmienionych przez executeUpdate()
        }
        catch (SQLException e)
        {
            var ee = prepareInfoAboutError(e,ErrorCodes.DELETE_ADDRESS_ERROR);
            throw ee;
        }
        finally {
            //zwolnienie zasobów
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

        //metoda używana w innych klasach DAO, do do pobrania informacji
        //nt. adresów do zbudowania adresów będących składowymi innych obiektów

        int addressId = rs.getInt(adrItr++);
        String street = rs.getString(adrItr++);
        int building = rs.getInt(adrItr++);
        int appartment = rs.getInt(adrItr++);

        //przypisz odpowiednią wartość w zależności od tego czy dana w bazie jest null-em
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
        String region = rs.getString(adrItr++);
        if(rs.wasNull())
        {
            region = Address.getNoRegion();
        }

        //utwórz adres
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
