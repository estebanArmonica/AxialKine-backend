package com.tiendaweb.strategys.impl;

import com.tiendaweb.models.Usuario;
import com.tiendaweb.strategys.ExcelReportBuilder;
import com.tiendaweb.strategys.ExportStrategy;
import com.tiendaweb.strategys.config.ExcelConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ExportStrategyImpl implements ExportStrategy<Usuario> {

    private final ExcelReportBuilder reportBuilder;

    public ExportStrategyImpl(ExcelReportBuilder reportBuilder) {
        this.reportBuilder = reportBuilder;
    }

    @Override
    public void export(HttpServletResponse response, List<Usuario> data, ExcelConfig config) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(config.getSheetName());

            // construimos el reporte
            reportBuilder.buildHeader(sheet, config);
            reportBuilder.buildBody(sheet, data);

            // configuramos la respuesta
            response.setContentType(config.getContentType());
            response.setHeader("Content-Disposition", "attachment; filename=" + config.getFileName());

            workbook.write(response.getOutputStream());
        }
    }
}
