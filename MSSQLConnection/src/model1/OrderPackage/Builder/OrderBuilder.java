package model1.OrderPackage.Builder;

import model1.Address.Address;
import model1.Customer.Customer;
import model1.Employee;
import model1.OrderPackage.ListInterfaces.ListSetters;
import model1.OrderPackage.OrderAndOrderInterfaces.*;
import model1.Product.Product;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderBuilder implements  IOrderBuilder{
    private Order orderToBuild;
    public OrderBuilder(Order orderToBuild)
    {
        this.orderToBuild = orderToBuild;
    }

    @Override
    public void setId(int id) {
        this.orderToBuild.setId(id);
    }

    @Override
    public void addProduct(Product product, int quantity) {
        orderToBuild.addProduct(product,quantity);
    }

    @Override
    public void setDeliveryAddress(Address address)  {
        orderToBuild.setDeliveryAddress(address);
    }

    @Override
    public void setEmployee(Employee employee)  {
        orderToBuild.setEmployee(employee);
    }

    @Override
    public void setBuyer(Customer customer)  {
        orderToBuild.setBuyer(customer);
    }

    @Override
    public void setDate(Timestamp date, DateType dateType) {
        if(dateType == DateType.order)
        {
            orderToBuild.setOrderDate(date);
        }
        else if(dateType == DateType.ship)
        {
            orderToBuild.setShipDate(date);
        }
        else if(dateType == DateType.require)
        {
            orderToBuild.setRequireDate(date);
        }
    }

    @Override
    public void setFreight(BigDecimal freight) {
        orderToBuild.setFreight(freight);
    }

    @Override
    public void setDescription(String description) {
        orderToBuild.setDescription(description);
    }

    private Order finishConstruction()
    {
        var retVal = orderToBuild;
        orderToBuild = null;
        return retVal;
    }
    @Override
    public OrderWithGetters returnOrderWithGetters() {
        return finishConstruction();
    }

    @Override
    public OrderWithSetters returnOrderWithSetters() {
        return finishConstruction();
    }

    @Override
    public ListSetters returnOrderWithListSetters() {
        return finishConstruction();
    }

    @Override
    public OrderWithAllMethods returnOrderWithAllMethods() {
        return finishConstruction();
    }


}
