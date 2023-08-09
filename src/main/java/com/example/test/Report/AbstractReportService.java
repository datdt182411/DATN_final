package com.example.test.Report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractReportService {
    protected DateFormat dateFormatter;
    public List<ReportItem> getReportDataLast7Days(ReportType reportType) {
        return getReportDataLastXDays(7, reportType);
    }

    public List<ReportItem> getReportDataLast28Days(ReportType reportType) {
        return getReportDataLastXDays(28, reportType);
    }

    protected List<ReportItem> getReportDataToday(ReportType reportType) {
        Date endTime = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date startTime = calendar.getTime();

        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    protected List<ReportItem> getReportDataLastXDays(int days, ReportType reportType) {
        Date endTime = new Date();

//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, -(days - 1));
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        Date startTime = calendar.getTime();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
        Date startTime = cal.getTime();

        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    public List<ReportItem> getReportDataLast6Months(ReportType reportType) {
        return getReportDataLastXMonths(6, reportType);
    }

    public List<ReportItem> getReportDataLastYear(ReportType reportType) {
        return getReportDataLastXMonths(12, reportType);
    }

    public List<ReportItem> getReportDataLastXMonths(int months, ReportType reportType) {
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -(months - 1));
        Date startTime = cal.getTime();

        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);

        dateFormatter = new SimpleDateFormat("yyyy-MM");


        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime, ReportType reportType){
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    protected abstract List<ReportItem> getReportDataByDateRangeInternal(Date startDate, Date endDate, ReportType reportType);
}
