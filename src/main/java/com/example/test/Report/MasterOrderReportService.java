package com.example.test.Report;

import com.example.test.Entity.Order;
import com.example.test.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MasterOrderReportService extends AbstractReportService{
    @Autowired
    private OrderRepository orderRepository;


    protected List<ReportItem> getReportDataByDateRangeInternal(Date startTime, Date endTime, ReportType reportType) {
        List<Order> orderList = orderRepository.findByOrderDateBetween(startTime, endTime);
        System.out.println("Check" + orderRepository.findByOrderDateBetween(startTime, endTime));
        System.out.println("Demo : ");
        printRawData( orderList);
        
         List<ReportItem> listReportItems = createReportData(startTime, endTime, reportType);


        calculateSalesForReportData(orderList, listReportItems);

        printReportData(listReportItems);
        System.out.println("Date: " + startTime);
        System.out.println("Test :" + listReportItems);
        
        return listReportItems;
    }

    protected List<ReportItem> getReportDataByTodayRangeInternal(Date startTime, Date endTime, ReportType reportType) {
        List<Order> orderList = orderRepository.findByOrderDateBetween(startTime, endTime);
        System.out.println("Check" + orderRepository.findByOrderDateBetween(startTime, endTime));
        System.out.println("Demo : ");
        printRawData( orderList);

        List<ReportItem> listReportItems = createTodayReportData(startTime, endTime, reportType);


        calculateSalesForReportData(orderList, listReportItems);

        printReportData(listReportItems);
        System.out.println("Date: " + startTime);
        System.out.println("Test :" + listReportItems);

        return listReportItems;
    }

    private List<ReportItem> createReportData(Date startTime, Date endTime, ReportType reportType) {
        List<ReportItem> listReportItems = new ArrayList<>();

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startTime);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endTime);

        Date currentDate = startDate.getTime();
        String dateString = dateFormatter.format(currentDate);

        listReportItems.add(new ReportItem(dateString));

        do {
            if (reportType.equals(ReportType.DAY)){
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            }else if(reportType.equals(ReportType.MONTH)) {
                startDate.add(Calendar.MONTH, 1);
            }
            currentDate = startDate.getTime();
            dateString = dateFormatter.format(currentDate);

            listReportItems.add(new ReportItem(dateString));

        } while (startDate.before(endDate));

        return listReportItems;
    }

    private List<ReportItem> createTodayReportData(Date startTime, Date endTime, ReportType reportType) {
        List<ReportItem> listReportItems = new ArrayList<>();

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startTime);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endTime);

        Date currentDate = startDate.getTime();
        String dateString = dateFormatter.format(currentDate);

        listReportItems.add(new ReportItem(dateString));

//        do {
            if (reportType.equals(ReportType.DAY)){
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            }else if(reportType.equals(ReportType.MONTH)) {
                startDate.add(Calendar.MONTH, 1);
            }

//      When delte 3 line so Not create column next day

//            currentDate = startDate.getTime();
//            dateString = dateFormatter.format(currentDate);
//
//            listReportItems.add(new ReportItem(dateString));

//        } while (startDate.before(endDate));

        return listReportItems;
    }

    private void calculateSalesForReportData( List<Order> orderList,  List<ReportItem> listReportItems) {
        for(Order order : orderList) {
            String orderDateString = dateFormatter.format(order.getOrderDate());

            ReportItem reportItem = new ReportItem(orderDateString);

            int itemIndex = listReportItems.indexOf(reportItem);

//            System.out.println("Check status Order: " + order.getStatus());

            if (itemIndex >= 0 && order.getStatus() == 2) {
                reportItem = listReportItems.get(itemIndex);
                reportItem.addGrossSales(order.getAmount());
                reportItem.increaseOrdersCount();
            }
        }
    }

    private void printReportData(List<ReportItem> listReportItems) {
        listReportItems.forEach(reportItem -> {
            System.out.printf("%s, %10.2f, %d \n", reportItem.getIdentifier(), reportItem.getGrossSales(), reportItem.getOrdersCount());
        });
    }


    private void printRawData(List<Order> orderList) {
        orderList.forEach(order -> {
            System.out.printf("%-3d | %s | %10.2f \n",
                    order.getId(), order.getOrderDate(), order.getAmount());
        });
    }
}
