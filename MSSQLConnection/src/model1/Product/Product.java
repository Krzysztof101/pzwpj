package model1.Product;

import model1.Category.Category;

public class Product implements Cloneable {
    private int id, quantity;
    private Category category;
    private String productName;
    private String unit;

    public int getId() {return id;}
    public Category getCategory()  { return (Category) category.clone(); }
    public  String getProductName() { return productName; }
    public String getUnit() { return unit;}
    public int getQuantity() { return quantity; }
    public Product(int id, Category category, String name, String unit, int quantity )  {
        this.id = id;
        this.category = (Category) category.clone();
        this.productName = name;
        this.unit = unit;
        this.quantity = quantity;
    }
    @Override
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;

        return id == ((Product)o).id;
    }
}
