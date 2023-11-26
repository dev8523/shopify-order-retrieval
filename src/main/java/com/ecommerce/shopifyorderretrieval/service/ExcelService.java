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
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"id", "admin_graphql_api_id", "app_id"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Populate data rows
            int rowNum = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowNum++);
                // Set cell values based on order object
                // For example:
                // row.createCell(0).setCellValue(order.getOrderNo());
                // row.createCell(1).setCellValue(order.getOrderDate());
                // ... set other cells ...
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
