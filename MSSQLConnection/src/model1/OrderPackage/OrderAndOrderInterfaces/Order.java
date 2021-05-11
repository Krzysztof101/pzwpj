package model1.OrderPackage.OrderAndOrderInterfaces;

import model1.Address.Address;
import model1.Customer.Customer;
import model1.Employee;
import model1.OrderPackage.Builder.OrderBuilder;
import model1.Product.Product;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;

public class Order implements  OrderWithAllMethods{
    private int id;
    private Customer buyer;
    private Employee employee;
    //TODO remove or comment employee int order classes
    private Timestamp orderDate, shipDate, requireDate;
    private BigDecimal freight;
    private String description;
    private Address deliveryAddress;
    private LinkedList<Product> products;
    private LinkedList<Integer> quantities;

    public static int getNoIdInDb() {
        return 0;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Order: id: ").append(id).append(", freight: ").append(freight).append(", description: ").append(description);
        builder.append("\ndelicery address: ").append(deliveryAddress).append("\nbuyer: ").append(buyer);
        builder.append("\n order date: ").append(orderDate).append("\nshipping date: ").append(shipDate).append("\nrequire date: ").append(requireDate).append("\n");
        for(int i=0; i<products.size(); i++)
        {
            builder.append(products.get(i)).append(", ordered: ").append(quantities.get(i)).append("\n");
        }
        return builder.toString();

    }



    private final static String NO_DESCRIPTION="";

    public static boolean noDescription(String dscptn) { return  dscptn.equals(NO_DESCRIPTION);}
    public static String getNoDescription()
    {
        return NO_DESCRIPTION;
    }
    @Override
    public void setNoDescription()
    {
        description=NO_DESCRIPTION;
    }

    @Override
    public int getId(){ return id;}
    @Override
    public boolean containsProduct(Product product)
    {
        return products.contains(product);
    }
    @Override
    public Product getProduct(int position)  {
        return (Product) products.get(position).clone();
    }
    @Override
    public int getQuantityOfProductAtPosition(int position)
    {
        return quantities.get(position);
    }
    @Override
    public int getQuantityOfProductAtPosition(Product product)
    {
        int pos=0;
        for(Product p :products)
        {

            if(p.equals(product))
            {
                return quantities.get(pos);
            }
            pos++;
        }
        return 0;
    }



    public Customer getBuyer()  { return (Customer) buyer.clone();}
    @Override
    public Employee getEmployee()  {
        return (Employee) employee.clone();
    }
    @Override
    public Timestamp getOrderDate() {
        return (Timestamp) orderDate.clone();
    }
    @Override
    public Timestamp getShipDate() {
        return (Timestamp) shipDate.clone();
    }
    @Override
    public Timestamp getRequireDate()
    {
        return (Timestamp) requireDate.clone();
    }
    @Override
    public BigDecimal getFreight() {return freight.add(BigDecimal.ZERO); }
    @Override
    public String getDesccription() {return description;}
    @Override
    public Address getDeliveryAddress()  { return (Address)deliveryAddress.clone(); }
    @Override
    public int size()
    {
        return products.size();
    }

    private Order()
    {
        products = new LinkedList<>();
        quantities = new LinkedList<>();
    }


    public static OrderBuilder create()
    {
        return new OrderBuilder(new Order());
    }

    @Override
    public void setId(int id) { this.id = id; }
    @Override
    public void setDescription(String description)
    {
        this.description = description;
    }
    @Override
    public void setBuyer(Customer buyer)  {
        this.buyer = (Customer) buyer.clone();
    }
    @Override
    public void setEmployee(Employee employee)  {
        this.employee = (Employee) employee.clone();
    }
    @Override
    public void setOrderDate(Timestamp orderDate)
    {
        this.orderDate = (Timestamp) orderDate.clone();
    }
    @Override
    public void setShipDate(Timestamp shipDate)
    {
        this.shipDate = (Timestamp) shipDate.clone();
    }
    @Override
    public void setRequireDate(Timestamp requireDate)
    {
        this.requireDate = (Timestamp) requireDate.clone();
    }
    @Override
    public void setFreight(BigDecimal freight)
    {
        this.freight = freight.add(BigDecimal.ZERO);
    }
    @Override
    public void setDeliveryAddress(Address address)  {
        this.deliveryAddress = (Address) address.clone();
    }

    @Override
    public void addProduct(Product product, int quantity)
    {
        //TODO add code responsible for null safety in addProduct
        products.add(product);
        quantities.add(quantity);

    }
    @Override
    public boolean removeProduct(Product product)
    {
        int position = products.indexOf(product);
        if(position!=-1)
        {
            products.remove(product);
            quantities.remove(position);
            return true;
        }
        return false;
    }
    @Override
    public boolean removeProduct(int index)
    {
        try {

            products.remove(index);
            quantities.remove(index);
            return true;
        }
        catch (IndexOutOfBoundsException e)
        {
            return false;
        }
    }
}


