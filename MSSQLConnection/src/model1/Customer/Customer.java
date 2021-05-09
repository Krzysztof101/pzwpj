package model1.Customer;


import model1.Address.Address;

public class Customer implements Cloneable {
    public Customer(int id, String companyName, Address address)
    {
        this.id = id;
        this.companyName = companyName;
        this.address = (Address) address.clone();
    }
    private int id;
    private String companyName;
    private Address address;
    public int getId(){return id;}
    public String getCompanyName(){ return  companyName;}
    public Address getAddress()  {return (Address) address.clone();}
    public  void setId(int id) { this.id = id; }
    public void  setCompanyName(String companyName) { this.companyName = companyName; }
    public void  setAddress(Address address) { this.address = (Address) address.clone() ; }
    @Override
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        //return new Customer(id,companyName, address);
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Customer: id: ").append(id).append(", companyName: ").append(companyName).append(", address").append(address);
        return builder.toString();
    }
}
