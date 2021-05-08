package model1.OrderPackage.ListInterfaces;

import model1.Product.Product;

public interface ListGetters {
    public boolean containsProduct(Product product);

    public Product getProduct(int position) ;

    public int getQuantityOfProductAtPosition(int position);

    public int getQuantityOfProductAtPosition(Product product);
}
