package com.ecommerce.shopifyorderretrieval.service;

import com.ecommerce.shopifyorderretrieval.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ExcelService {
    public void generateExcel(List<Order> orders, String filePath) {
        log.info("Order details: {}", orders); // TODO remove this log

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Order No", "Order Date", "Customer Name", "Customer Mobile", "Customer Email",
                    "Customer City", "Customer State", "Customer Pincode", "Customer Address",
                    "Dispatch Date", "Current Status", "Delivery Status",
                    "Order Amount", "Order Quantity", "Order Type"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Populate data rows
            int rowNum = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowNum++);
                log.info("Setting cell values for order: {}", order); // TODO remove this log
                // Set cell values based on order object
                row.createCell(0).setCellValue(order.getOrderNo());
                row.createCell(1).setCellValue(order.getOrderDate());
                row.createCell(2).setCellValue(
                        order.getCustomer().getCustomerFirstName()
                        + " "
                        + order.getCustomer().getCustomerLastName()
                );
                row.createCell(3).setCellValue(order.getCustomer().getCustomerPhone());
                row.createCell(4).setCellValue(order.getCustomer().getCustomerEmail());
            }

            // Write the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                log.info("Excel file generated successfully at path: {}", filePath);
            }
        } catch (IOException e) {
            log.error("Error generating Excel file: {}", e.getMessage());
        }
    }
}
