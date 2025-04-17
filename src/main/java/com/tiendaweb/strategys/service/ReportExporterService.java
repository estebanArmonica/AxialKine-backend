package com.tiendaweb.strategys.service;

import com.tiendaweb.models.Usuario;
import com.tiendaweb.repositories.IUsuarioRepository;
import com.tiendaweb.strategys.ExportStrategy;
import com.tiendaweb.strategys.config.ExcelConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReportExporterService {
    private final ExportStrategy excelExport;
    private final IUsuarioRepository userRepo;

    public ReportExporterService(ExportStrategy excelExport, IUsuarioRepository userRepo) {
        this.excelExport = excelExport;
        this.userRepo = userRepo;
    }

    public void exportUsuarioExcel(HttpServletResponse response, Long rolId) throws IOException {
        List<Usuario> usuarios = userRepo.findByRolId(rolId);
        ExcelConfig config = ExcelConfig.defaultUsuarioConfig();

        excelExport.export(response, usuarios, config);
    }
}
