package model1.OrderPackage.OrderAndOrderInterfaces;

import model1.Address.Address;
import model1.Customer.Customer;
import model1.Employee;
import model1.OrderPackage.ListInterfaces.ListGetters;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface OrderWithGetters extends ListGetters {


    public Customer getBuyer() ;
    public Employee getEmployee() ;

    public Timestamp getOrderDate();
    int getId();
    public Timestamp getShipDate();
    public Timestamp getRequireDate();
    public BigDecimal getFreight() ;
    public String getDesccription() ;
    public Address getDeliveryAddress() ;
    public int size();



}

