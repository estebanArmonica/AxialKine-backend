package com.tiendaweb.strategys;

import com.tiendaweb.models.Usuario;
import com.tiendaweb.strategys.config.ExcelConfig;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Component
public class ExcelReportBuilder {
    public void buildHeader(Sheet sheet, ExcelConfig config) {
        Row headerRow = sheet.createRow(0);
        IntStream.range(0, config.getHeaders().size())
                .forEach(i -> headerRow.createCell(i).setCellValue(config.getHeaders().get(i)));
    }

    public void buildBody(Sheet sheet, List<Usuario> usuarios) {
        AtomicInteger rowNum = new AtomicInteger(1);
        usuarios.forEach(usuario -> {
            Row row = sheet.createRow(rowNum.getAndIncrement());
            row.createCell(0).setCellValue(usuario.getId());
            row.createCell(1).setCellValue(usuario.getRut());
            row.createCell(2).setCellValue(usuario.getDv());
            row.createCell(3).setCellValue(usuario.getNombre());
            row.createCell(4).setCellValue(usuario.getApellido());
            row.createCell(5).setCellValue(usuario.getEmail());
            row.createCell(6).setCellValue(usuario.getUsername());
        });
    }
}
