package com.company.inventory.inventario.util;

import com.company.inventory.inventario.model.Product;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.util.List;

public class ProductExcelExporter {

    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet sheet;
    private List<Product> products;

    public ProductExcelExporter(List<Product> products) {
        this.products = products;
        xssfWorkbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = xssfWorkbook.createSheet("Products");
        
        Row row = sheet.createRow(0);
        CellStyle style = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Nome", style);
        createCell(row, 2, "Preço", style);
        createCell(row, 3, "Quantidade", style);
        createCell(row, 4, "Categoria", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }


    private void writeDataLines() {
        CellStyle style = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        int rowCount = 1;
        for (Product result : products) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, String.valueOf(result.getId()), style);
            createCell(row, columnCount++, result.getName(), style);
            createCell(row, columnCount++, result.getPrice(), style);
            createCell(row, columnCount++, result.getAccount(), style);
            createCell(row, columnCount++, result.getCategory().getName(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        try {
            writeHeaderLine(); // Write header line
            writeDataLines(); // Write data lines

            ServletOutputStream servletOutput = response.getOutputStream();
            xssfWorkbook.write(servletOutput);
            xssfWorkbook.close();

            servletOutput.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
