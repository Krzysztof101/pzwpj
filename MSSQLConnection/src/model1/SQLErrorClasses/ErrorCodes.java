package model1.SQLErrorClasses;

public class ErrorCodes {
    public final static String SEPARATOR="*";
    public final static int UPDATE_ADDRESS_ERROR = 1001;
    public final static int DELETE_ADDRESS_ERROR = 1002;
    public final static int INSERT_CATEGORY_ERROR = 1003;
    public final static int UPDATE_CATEGORY_ERROR = 1004;
    public final static int SELECT_ALL_CATEGORY_ERROR = 1005;
    public final static int SELECT_SINGLE_CATEGORY_ERROR = 1006;
    public final static int DELETE_CATEGORY_ERROR = 1007;
    public final static int INSERT_SUPPLIER_ERROR = 1008;
    public final static int UPDATE_SUPPLIER_ERROR=1009;
    public final static int SELECT_ALL_SUPPLIER_ERROR=1010;
    public final static int INSERT_PRODUCT_ERROR=1013;
    public final static int SELECT_SINGLE_PRODUCT_ERROR = 1014;
    public final static int SELECT_ALL_PRODUCT_ERROR = 1015;
    public final static int DELETE_PRODUCT_ERROR=1016;
    public final static int INSERT_CUSTOMER_ERROR=1017;
    public final static int SELECT_SINGLE_CUSTOMER_ERROR=1018;
    public final static int SELECT_ALL_CUSTOMER_ERROR=1019;
    public final static int DELETE_CUSTOMER_ERROR=1020;
    public final static int INSERT_ORDER_ERROR=1021;
    public final static int DELETE_ORDER_ERROR=1022;
    public final static int SELECT_SINGLE_ORDER_ERROR=1023;
    //TODO ADD UPDATE PRODUCT I UPDATE CUSTOMER

    public static String getMessage(int errorCode)
    {
        switch (errorCode)
        {


            case UPDATE_ADDRESS_ERROR:
            {
                return "Error while updating db"+SEPARATOR;
            }
            case DELETE_ADDRESS_ERROR:
            {
                return "Error while deleting from db"+SEPARATOR;
            }
            case INSERT_CATEGORY_ERROR:
            {
                return "Error while inserting to category"+SEPARATOR;
            }
            case UPDATE_CATEGORY_ERROR:
            {
                return "Error while updating category"+SEPARATOR;
            }
            case SELECT_ALL_CATEGORY_ERROR:
            {
                return "Error while selecting all categories"+SEPARATOR;
            }
            case SELECT_SINGLE_CATEGORY_ERROR:
                return "Error while selecting single category"+SEPARATOR;
            case DELETE_CATEGORY_ERROR:
                return "Error while deleting from category"+SEPARATOR;
            case INSERT_SUPPLIER_ERROR:
                return "Error while inserting to Supplier"+SEPARATOR;
            case UPDATE_SUPPLIER_ERROR:
                return "Error while updating Supplier"+SEPARATOR;
            case SELECT_ALL_SUPPLIER_ERROR:
                return "Error while selecting all Suppliers"+SEPARATOR;
            case INSERT_PRODUCT_ERROR:
                return "Error while creating product"+SEPARATOR;
            case SELECT_SINGLE_PRODUCT_ERROR:
                return "Error while selecting single product"+SEPARATOR;
            case SELECT_ALL_PRODUCT_ERROR:
                return "Error while selecting all products"+SEPARATOR;
            case DELETE_PRODUCT_ERROR:
                return "Error while deleting Product"+SEPARATOR;
            case SELECT_SINGLE_CUSTOMER_ERROR:
                return "SELECT_SINGLE_CUSTOMER_ERROR"+SEPARATOR;
            case SELECT_ALL_CUSTOMER_ERROR:
                return "SELECT_ALL_CUSTOMER_ERROR"+SEPARATOR;
            case DELETE_CUSTOMER_ERROR:
                return "DELETE_CUSTOMER_ERROR"+SEPARATOR;
            case INSERT_ORDER_ERROR:
                return "INSERT_ORDER_ERROR"+SEPARATOR;
            case DELETE_ORDER_ERROR:
                return "DELETE_ORDER_ERROR"+SEPARATOR;
            case SELECT_SINGLE_ORDER_ERROR:
                return "SELECT_SINGLE_ORDER_ERROR"+SEPARATOR;
                default:
            {
                return "Unknown error"+SEPARATOR;
            }
        }

    }

    public static String getSeparator(){return SEPARATOR;}

}
