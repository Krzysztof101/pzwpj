package model1.OrderPackage.OrderAndOrderInterfaces;

import model1.Address.Address;
import model1.Customer.Customer;
import model1.Employee;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface OrderWithSetters extends OrderWithGetters {
    void setDescription(String description);
    void setBuyer(Customer buyer) ;
    void setEmployee(Employee employee) ;
    void setOrderDate(Timestamp orderDate);
    void setShipDate(Timestamp shipDate);
    void setRequireDate(Timestamp requireDate);
    void setFreight(BigDecimal freight);
    void setDeliveryAddress(Address address);
    void setNoDescription();
    void setId(int id);

}
