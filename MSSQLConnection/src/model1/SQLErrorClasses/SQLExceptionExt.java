package model1.SQLErrorClasses;

import java.sql.SQLException;

public class SQLExceptionExt extends SQLException {
    public String getErrorMessage()
    {
        return ErrorCodes.getMessage(errCode);
    }
    public int getErrorCode()
    {
        return errCode;
    }
    private int errCode;
    public SQLExceptionExt(SQLException sqlException, int errorCode)
    {
        super(ErrorCodes.getMessage(errorCode)+sqlException.getMessage(),sqlException.getSQLState(),sqlException.getErrorCode());
        errCode = errorCode;
    }
}
