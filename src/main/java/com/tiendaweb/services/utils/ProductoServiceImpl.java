package com.tiendaweb.services.utils;

import com.tiendaweb.models.Producto;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.services.IProductoService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Blob;
import java.util.*;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private IProductoRepository productRepo;

    public ProductoServiceImpl(IProductoRepository productRepo){
        this.productRepo = productRepo;
    }

    @Override
    public Producto agregarProducto(Producto prod) {
        return productRepo.save(prod);
    }

    // creamos el codigo de forma unica por un metodo privado
    private int generarCodigoUnico() {
        Random random = new Random();
        int codigo;
        do{
            // genera un numero entre el 999 y 9999
            codigo = 1000 + random.nextInt(9000);
        }while(productRepo.existsByCodigo(codigo)); // nos aseguramos de el codigo no exista

        return codigo;
    }

    @Override
    public Producto updateProducto(int codigo, Producto prod) {
        Producto producto = productRepo.findById(codigo).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setNombre(prod.getNombre());
        producto.setPrecio(prod.getPrecio());
        producto.setImagen(prod.getImagen());
        producto.setCreado(prod.getCreado());
        producto.setTerminado(prod.getTerminado());
        producto.setDescripcion(prod.getDescripcion());
        producto.setEstado(prod.getEstado());
        producto.setCate(prod.getCate());

        return productRepo.save(prod);
    }

    @Override
    public Set<Producto> obtenerTodo() {
        return new LinkedHashSet<>(productRepo.findAll());
    }

    @Override
    public void exportExcel(final HttpServletResponse response) throws IOException, Exception {
        final List<Producto> productos = this.productRepo.findAll();

        final Workbook workbook = new XSSFWorkbook();
        final Sheet sheet = workbook.createSheet("productos");

        final Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("CODIGO");
        header.createCell(1).setCellValue("NOMBRE");
        header.createCell(2).setCellValue("PRECIO");
        header.createCell(3).setCellValue("FEC_CREADO");
        header.createCell(4).setCellValue("FEC_TERMINO");
        header.createCell(5).setCellValue("DESCRIPCIÓN");
        header.createCell(6).setCellValue("CATEGORIA");
        header.createCell(7).setCellValue("ESTADO");

        // creamos el estilo para fecha
        CellStyle dateStyle = workbook.createCellStyle();
        DataFormat dateFormat = workbook.createDataFormat();
        dateStyle.setDataFormat(dateFormat.getFormat("yyyy-mm-dd"));

        int rowNum = 1;
        for(final Producto prod: productos){
            final Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(prod.getCodigo());
            row.createCell(1).setCellValue(prod.getNombre());
            row.createCell(2).setCellValue(prod.getPrecio());

            // usamos la fecha con estilo
            Cell createdCell = row.createCell(3);
            row.createCell(3).setCellValue(prod.getCreado());
            createdCell.setCellStyle(dateStyle);

            Cell finishedCell = row.createCell(4);
            row.createCell(4).setCellValue(prod.getTerminado());
            finishedCell.setCellStyle(dateStyle);

            row.createCell(5).setCellValue(prod.getDescripcion());
            row.createCell(6).setCellValue(prod.getCate().getDescripcion());
            row.createCell(7).setCellValue(prod.getEstado().get(0).getTipo());
        }

        final ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    @Override
    public Optional<Producto> buscarPorCodigo(int codigo) {
        return productRepo.findByCodigo(codigo);
    }
}
