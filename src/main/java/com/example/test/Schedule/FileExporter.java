package com.example.test.Schedule;


import com.example.test.Entity.MaintenanceOrder;
import com.example.test.Service.MaintenanceOrderService;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class FileExporter {

    private   XSSFWorkbook workbook ;
    private  XSSFSheet sheet ;

    private final MaintenanceOrderService maintenanceOrderService;

    public FileExporter(MaintenanceOrderService maintenanceOrderService) {
        this.maintenanceOrderService = maintenanceOrderService;
        workbook = new XSSFWorkbook();
    }



    public void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        sheet.setVerticallyCenter(true);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);

    }



    @Scheduled(cron = "0 53 10 * * ?")  // Scheduled to run every day at 10 AM
    public void exportFile() {
        // Logic to export the file
        // Replace this with your actual file export implementation
        // For example, using Apache POI
//        List<Maintenance> maintenanceList = maintenanceService.listAll();
        try {
            sheet = workbook.createSheet("Maintenance");
            XSSFRow row = sheet.createRow(0);

            XSSFCellStyle cellStyle = workbook.createCellStyle();

            XSSFFont font = workbook.createFont();

            font.setBold(true);
            font.setFontHeight(16);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            createCell(row, 0, "Mã bảo hành ", cellStyle);
            createCell(row, 1, "Mã chi tiết đơn hàng ", cellStyle);
            createCell(row, 2, "Tên sản phẩm", cellStyle);
            createCell(row, 3, "Tên khách hàng", cellStyle);
            createCell(row, 4, "Số điện thoại", cellStyle);
            createCell(row, 5, "Địa chỉ ", cellStyle);

            int rowIndex = 1;
            List<MaintenanceOrder> maintenanceOrderList = maintenanceOrderService.listAll();
            XSSFCellStyle cellStyle1 = workbook.createCellStyle();
            XSSFFont font1 = workbook.createFont();
            font1.setBold(false);
            font1.setFontHeight(14);
            cellStyle1.setFont(font1);
            cellStyle1.setAlignment(HorizontalAlignment.CENTER);

            LocalDate now = LocalDate.now();

            for (MaintenanceOrder maintenance : maintenanceOrderList) {
                if (maintenance.getMaintenanceDate().compareTo(String.valueOf(now)) == 0) {
                        row = sheet.createRow(rowIndex++);
                        int columnIndex = 0;

                        createCell(row, columnIndex++, maintenance.getId(), cellStyle1);
                        createCell(row, columnIndex++, maintenance.getOrderLine().getId(), cellStyle1);
                        createCell(row, columnIndex++, maintenance.getOrderLine().getProduct().getName(), cellStyle1);
                        createCell(row, columnIndex++, maintenance.getOrderLine().getOrder().getCustomer().getName(), cellStyle1);
                        createCell(row, columnIndex++, maintenance.getOrderLine().getOrder().getCustomer().getPhone(), cellStyle1);
                        createCell(row, columnIndex, maintenance.getOrderLine().getOrder().getCustomer().getAddress(), cellStyle1);
//                createCell(row, columnIndex, maintenance.getCustomerName(), cellStyle);
                    }
                }


            FileOutputStream fileOut = new FileOutputStream("Danh muc san pham bao tri " + now + ".xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            System.out.println("File exported successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting file: " + e.getMessage());
        }
    }
}