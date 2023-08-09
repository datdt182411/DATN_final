package com.example.test.Report;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Data
public class ReportItem {
    private String identifier;

    private float grossSales;

    private float grossTypeSales;
    private int ordersCount;

    private int productCount;

    private String typeName;

    int statusOrder;



    public ReportItem() {
    }

    public ReportItem(String identifier) {
        this.identifier = identifier;
    }

    public ReportItem(String identifier, float grossSales) {
        this.identifier = identifier;
        this.grossSales = grossSales;
    }

    public ReportItem(String identifier, String typeName, float grossSales, int productCount , float grossTypeSales) {
        this.identifier = identifier;
        this.typeName = typeName;
        this.grossSales = grossSales;
        this.productCount = productCount;
        this.grossTypeSales = grossTypeSales;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return  true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReportItem other = (ReportItem) obj;
        return Objects.equals(identifier, other.identifier);
    }


    public void addGrossSales(double amount) {
        this.grossSales += amount;
    }

    public void addTypeGrossSales(double amount) {
        this.grossTypeSales += amount;
    }

    public void increaseOrdersCount() {
        this.ordersCount++;
    }

    public void increaseProductsCount(int count) {
        this.productCount += count;
    }
}
