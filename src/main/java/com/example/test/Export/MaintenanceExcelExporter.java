package com.example.test.Export;

import com.example.test.Entity.MaintenanceOrder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MaintenanceExcelExporter extends AbstractExporter {

    private   XSSFWorkbook workbook ;
    private  XSSFSheet sheet ;



    public MaintenanceExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    public void writeHeaderLine() {
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
//        createCell(row, 5, "Địa chỉ", cellStyle);
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

    public void export(List<MaintenanceOrder> maintenanceOrderList, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response, "application/octet-stream", ".xlsx");

        writeHeaderLine();
        writeDataLines(maintenanceOrderList);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public void writeDataLines(List<MaintenanceOrder> maintenanceOrderList) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        LocalDate now = LocalDate.now();

        for (MaintenanceOrder maintenance : maintenanceOrderList) {
            if(maintenance.getMaintenanceDate().compareTo(String.valueOf(now)) == 0 ) {
                XSSFRow row = sheet.createRow(rowIndex++);
                int columnIndex = 0;

                createCell(row, columnIndex++, maintenance.getId(), cellStyle);
                createCell(row, columnIndex++, maintenance.getOrderLine().getId(), cellStyle);
                createCell(row, columnIndex++, maintenance.getOrderLine().getProduct().getName(), cellStyle);
                createCell(row, columnIndex++, maintenance.getOrderLine().getOrder().getCustomer().getName(), cellStyle);
                createCell(row, columnIndex++, maintenance.getOrderLine().getOrder().getCustomer().getPhone(), cellStyle);
                createCell(row, columnIndex, maintenance.getOrderLine().getOrder().getCustomer().getAddress(), cellStyle);
//                createCell(row, columnIndex, maintenance.getCustomerName(), cellStyle);
            }
        }
    }
}
