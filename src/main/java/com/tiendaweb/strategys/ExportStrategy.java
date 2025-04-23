package com.tiendaweb.strategys;

import com.tiendaweb.strategys.config.ExcelConfig;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface ExportStrategy<T> {
    // creacion de excel
    void export(HttpServletResponse response, List<T> data, ExcelConfig config) throws IOException;

}
