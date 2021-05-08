package model1.SupplyPackage;

import model1.Supplier.Supplier;

import java.sql.Timestamp;

public interface SupplyWithGetters {
    public int getId();
    public Timestamp getDeliveryDate();
    public Timestamp getOrderDate();
    public Supplier getSupplier();
}
