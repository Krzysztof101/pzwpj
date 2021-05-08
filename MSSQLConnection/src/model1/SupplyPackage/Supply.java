package model1.SupplyPackage;

import model1.Product.Product;
import model1.Supplier.Supplier;

import java.sql.Timestamp;
import java.util.LinkedList;

public class Supply implements SupplyWithAllMethods {
    private int id;
    private Timestamp deliveryDate;
    private Timestamp orderDate;
    private Supplier supplier;
    private LinkedList<Product> products;
    private LinkedList<Integer> quantities;
    public  Supply(int id, Timestamp deliveryDate, Timestamp orderDate)
    {
        this.id = id;
        this.deliveryDate = (Timestamp) deliveryDate.clone();
        this.orderDate = (Timestamp) orderDate.clone();
        products = new LinkedList<>();
        quantities = new LinkedList<>();
    }

    @Override
    public int getId()
    {
        return id;
    }
    @Override
    public Timestamp getDeliveryDate()
    {
        return (Timestamp) deliveryDate.clone();
    }
    @Override
    public Timestamp getOrderDate()
    {
        return (Timestamp) orderDate.clone();
    }
    @Override
    public Supplier getSupplier()
    {
        return (Supplier) supplier.clone();
    }

    @Override
    public void addProduct(Product product, int quantity)  {
        products.add( (Product)product.clone() );
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

    @Override
    public boolean containsProduct(Product product)
    {
        return products.contains(product);
    }
    @Override
    public Product getProduct(int index)  {
        return  (Product) products.get(index).clone();
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
}
