package com.product.management.utils;

import com.product.management.dto.RegisterRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ExcelHelper {

    public static List<RegisterRequest> excelToRegisterRequests(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            List<RegisterRequest> list = new ArrayList<>();

            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Skip header row
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                RegisterRequest request = new RegisterRequest();

                // Example: assuming columns order: name, email, password, roleId, tenantId
                request.setName(getCellValueAsString(currentRow.getCell(0)));
                request.setEmail(getCellValueAsString(currentRow.getCell(1)));
                request.setPassword(getCellValueAsString(currentRow.getCell(2)));

                String roleIdStr = getCellValueAsString(currentRow.getCell(3));
                request.setRoleId(Math.toIntExact(roleIdStr != null ? Long.parseLong(roleIdStr) : null));

                String tenantIdStr = getCellValueAsString(currentRow.getCell(4));
                if (tenantIdStr != null && !tenantIdStr.isEmpty()) {
                    request.setTenantId(UUID.fromString(tenantIdStr));
                } else {
                    request.setTenantId(null);
                }

                list.add(request);
            }

            workbook.close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return null;
        }
    }
    public static boolean hasExcelFormat(MultipartFile file) {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(file.getContentType());
    }

}
