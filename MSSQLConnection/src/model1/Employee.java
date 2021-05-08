package model1;

import model1.Address.Address;

import java.sql.Timestamp;

public class Employee implements Cloneable{
    private int id;
    private String firstName, lastName;
    private Timestamp hireDate, fireDate, birthDate;
    private Address address;
    public int getId()
    {
        return id;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public Employee(int id, String firstName, String lastName, Timestamp hire, Timestamp fire, Timestamp birth, Address address)  {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        hireDate = (Timestamp) hire.clone();
        fireDate = (Timestamp) fire.clone();
        birthDate = (Timestamp) birth.clone();
        this.address = (Address) address.clone();
    }
    public Timestamp getHireDate()
    { return (Timestamp)hireDate.clone(); }
    public Timestamp getFireDate()
    { return (Timestamp)fireDate.clone(); }
    public Timestamp getBirthDate()
    { return (Timestamp) birthDate.clone();}
    public Address getAddress() { return (Address) address.clone();}
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
