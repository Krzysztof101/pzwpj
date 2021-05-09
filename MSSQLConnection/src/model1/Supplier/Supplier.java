package model1.Supplier;

import model1.Address.Address;

public class Supplier implements Cloneable{
    private int id;
    private String name;
    private Address address;
    private final static int NO_ID_IN_DATABASE = 0;
    public Supplier(int id, String name, Address address)  {
        this.id = id;
        this.name = name;
        this.address = (Address) address.clone();
    }
    public Supplier(String name, Address address)
    {
        this.id = NO_ID_IN_DATABASE;
        this.name = name;
        this.address = (Address) address.clone();
    }
    public void setAddress(Address address){ this.address = (Address) address.clone();}
    public void setId(int id) { this.id = id;}
    public void setName(String name){this.name = name;}
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
    public String toString()
    {
        return "Supplier: id: "+Integer.toString(id)+", name: "+name+", address: "+address;
    }

}
