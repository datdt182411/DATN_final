package com.example.test.Report;

import com.example.test.Entity.OrderLine;
import com.example.test.Repository.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderDetailReportService extends AbstractReportService{

    @Autowired
    private OrderLineRepository repo;

    @Override
    protected List<ReportItem> getReportDataByDateRangeInternal(
            Date startDate, Date endDate, ReportType reportType) {
        List<OrderLine> orderLineList = null;

        if (reportType.equals(ReportType.CATEGORY)) {
            orderLineList = repo.findWithTypeAndTimeBetween(startDate, endDate);

        }
        else if (reportType.equals(ReportType.PRODUCT)) {
            orderLineList = repo.findWithProductAndTimeBetween(startDate, endDate);
        }

        System.out.println("Print RawData \n");
        printRawData(orderLineList);

        List<ReportItem> reportItemList = new ArrayList<>();



        for (OrderLine orderLine : orderLineList) {
//            System.out.println("Name product: " + orderLine.getProduct().getName());
//
//            System.out.println("Price product: " + orderLine.getProduct().getPrice());
//
//            System.out.println("Order status: " +  orderLine.getOrder().getStatus());

            String identifier = "";
            if(reportType.equals(ReportType.CATEGORY)) {
                identifier = orderLine.getProduct().getType().getName();
            }

            else if (reportType.equals(ReportType.PRODUCT)) {
                identifier = orderLine.getProduct().getName();
            }
            ReportItem reportItem = new ReportItem(identifier);

            int itemIndex = reportItemList.indexOf(reportItem);

            if (itemIndex >= 0 && orderLine.getOrder().getStatus() == 2){
                reportItem = reportItemList.get(itemIndex);
                reportItem.increaseProductsCount(orderLine.getQuantity());

                if (reportType.equals(ReportType.CATEGORY)){
                    reportItem.addTypeGrossSales(orderLine.getQuantity() * orderLine.getProduct().getPrice());
                }

            } else if(orderLine.getOrder().getStatus() == 2) {
                reportItemList.add(new ReportItem(identifier, orderLine.getProduct().getType().getName(), (float)orderLine.getProduct().getPrice(), orderLine.getQuantity(), (float) (orderLine.getQuantity() * orderLine.getProduct().getPrice())));
            }
        }


            for (ReportItem item : reportItemList) {
                System.out.printf("%-20s, %-5s, %10.2f, %d , %d, %10.2f \n"
                        , item.getIdentifier(), item.getTypeName(), item.getGrossSales(), item.getProductCount(), item.getStatusOrder(), item.getGrossTypeSales());
                if(reportType.equals(ReportType.PRODUCT)) {
                    item.setGrossSales(item.getGrossSales() * item.getProductCount());
                }

            }


        return reportItemList;
    }

//    private void printReportData(List<ReportItem> reportItemList) {
//        for (ReportItem item : reportItemList) {
//            System.out.printf("%-20s, %10.2f, %d  \n"
//                , item.getIdentifier(), item.getGrossSales(), item.getProductCount());
//        }
//    }

// <!-- Display list of Category -->
//    private void printRawData(List<OrderLine> orderLineList) {
//        for (OrderLine orderLine : orderLineList) {
//            System.out.printf("%d | %-20s  \n", orderLine.getQuantity()
//            , orderLine.getProduct().getType().getName());
//        }
//    }

    private void printRawData(List<OrderLine> orderLineList) {
        for (OrderLine orderLine : orderLineList) {
            System.out.printf("%d | %-20s | %10.2f  \n", orderLine.getQuantity()
                    , orderLine.getProduct().getName() , orderLine.getProduct().getPrice());
        }
    }
}
