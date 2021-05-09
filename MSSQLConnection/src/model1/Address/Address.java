package model1.Address;

public class Address implements Cloneable{
    private int id;
    private String street;
    private int buildingNumber;
    private int appartmentNumber;
    private String appartmentNumberAppendix;
    private String city;
    private String country;
    private String postalCode;
    private String region;

    public String getStreet(){return street;}
    public int getId() { return id;}
    public void setStreet(String street){this.street = street;}
    public int getBuildingNumber(){return buildingNumber;}
    public void setBuildingNumber(int number){buildingNumber = number;}
    public int getAppartmentNumber() {return  appartmentNumber;}
    public void setAppartmentNumber(int number) { appartmentNumber = number;}
    public String getAppartmentNumberAppendix() { return  appartmentNumberAppendix;}
    public void setAppartmentNumberAppendix(String appendix) {appartmentNumberAppendix =appendix; }
    public String getCity() {return  city;}
    public void setCity(String city) { this.city = city;}
    public String getPostalCode() {return  postalCode;}
    public void setPostalCode(String code) { postalCode = code; }
    public String getCountry(){return country;}
    public void setCountry(String country) {this.country = country;}
    public String getRegion() {return  region;}
    public void  setRegion(String region){this.region = region;}
    public Address(int id) {
        this.id = id;
        appartmentNumber=Address.getNoAppartmentNumber();
        appartmentNumberAppendix=Address.getNoAppartmentNumberAppendix();
        region = Address.getNoRegion();
    }
    public boolean noAppartmentNumber() { return  appartmentNumber == NO_APPARTMENT;}
    public boolean noAppartmentNumberAppendix() { return appartmentNumberAppendix.equals(NO_APPARTMENT_APPENDIX);}
    public boolean noRegion() { return region.equals(NO_REGION);}

    public static final int getNoAppartmentNumber(){return NO_APPARTMENT;}
    public static final String getNoAppartmentNumberAppendix(){return NO_APPARTMENT_APPENDIX;}
    public static final String getNoRegion(){return NO_REGION;}
    private static final String NO_REGION="";
    private static  final String NO_APPARTMENT_APPENDIX="";
    private static  final int NO_APPARTMENT=-1;


    @Override
    public Object clone()
    {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        /*
        Address copy = new Address(id);
        copy.setStreet(street);
        copy.setBuildingNumber(buildingNumber);
        copy.setAppartmentNumber(appartmentNumber);
        copy.setAppartmentNumberAppendix(appartmentNumberAppendix);
        copy.setCity(city);
        copy.setPostalCode(postalCode);
        copy.setCountry(country);
        copy.setRegion(region);
        return copy;
        */
    }
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("id: ").append(id).append(", street: ").append(street).append(" ").append(buildingNumber);
        if(appartmentNumber!= NO_APPARTMENT)
        {
            builder.append("/").append(appartmentNumber);
        }
        builder.append(appartmentNumberAppendix).append(", ").append(postalCode).append(" ").append(city).append(", region: ").append(region).append(" ").append(country);
        return builder.toString();
    }

    public void setId(int newId) {
        this.id = newId;
    }

}
