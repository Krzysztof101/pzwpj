package model1.Supplier;

import model1.Address.Address;

public class Supplier implements Cloneable{
    private int id;
    private String name;
    private Address address;
    public Supplier(int id, String name, Address address)  {
        this.id = id;
        this.name = name;
        this.address = (Address) address.clone();
    }
    public void setAddress(Address address){ this.address = (Address) address.clone();}
    public int getId()
    {
        return id;
    }
    public String getCompanyName()
    {
        return name;
    }
    public Address getAddress()  {
        return (Address) address.clone();
    }
    @Override
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
