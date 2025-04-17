package com.tiendaweb.strategys.config;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExcelConfig {
    private String sheetName;
    private String fileName;
    private String contentType;
    private List<String> headers;

    public static ExcelConfig defaultUsuarioConfig() {
        return ExcelConfig.builder()
                .sheetName("Clientes")
                .fileName("clientes.xlsx")
                .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .headers(List.of("ID", "RUT", "DV", "NOMBRES", "APELLIDOS", "EMAIL", "USERNAME"))
                .build();
    }
}
