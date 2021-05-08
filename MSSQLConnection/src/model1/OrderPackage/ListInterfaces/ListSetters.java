package model1.OrderPackage.ListInterfaces;

import model1.Product.Product;

public interface ListSetters
{
    public void addProduct(Product product, int quantity) ;
    public boolean removeProduct(Product product);
    public boolean removeProduct(int index);
}
