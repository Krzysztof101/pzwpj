package model1.OrderPackage.Builder;
import model1.Address.Address;
import model1.Customer.Customer;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithAllMethods;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithGetters;
import model1.OrderPackage.ListInterfaces.ListSetters;
import model1.OrderPackage.OrderAndOrderInterfaces.OrderWithSetters;
import model1.Product.Product;
import model1.Employee;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface IOrderBuilder {

    public void setId(int id);
    public void addProduct(Product product, int quantity);
    public void setDeliveryAddress(Address address) throws CloneNotSupportedException;
    public void setEmployee(Employee employee) throws CloneNotSupportedException;
    public void setBuyer(Customer customer) throws CloneNotSupportedException;
    public void setDate(Timestamp date, DateType dateType);
    public void setFreight(BigDecimal freight);
    public void setDescription(String description);
    public OrderWithGetters returnOrderWithGetters();
    public OrderWithSetters returnOrderWithSetters();
    public ListSetters returnOrderWithListSetters();
    public OrderWithAllMethods returnOrderWithAllMethods();



}
