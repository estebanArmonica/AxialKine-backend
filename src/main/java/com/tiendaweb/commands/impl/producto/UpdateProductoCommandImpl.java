package com.tiendaweb.commands.impl.producto;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.impl.tarea.utils.BlobCreator;
import com.tiendaweb.exception.BusinessException;
import com.tiendaweb.exception.ResourceNotFoundException;
import com.tiendaweb.exception.ValidationException;
import com.tiendaweb.models.Categoria;
import com.tiendaweb.models.Estado;
import com.tiendaweb.models.Producto;
import com.tiendaweb.repositories.ICategoriaRepository;
import com.tiendaweb.repositories.IEstadoRepository;
import com.tiendaweb.repositories.IProductoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class UpdateProductoCommandImpl implements Command<Producto> {
    private final int codigo;
    private final Producto producto;
    private final MultipartFile imagenFile;
    private final IProductoRepository productoRepository;
    private final ICategoriaRepository categoriaRepository;
    private final IEstadoRepository estadoRepository;
    private final BlobCreator blobCreator;

    public UpdateProductoCommandImpl(int codigo, Producto producto, MultipartFile imagenFile, IProductoRepository productoRepository, ICategoriaRepository categoriaRepository, IEstadoRepository estadoRepository, BlobCreator blobCreator) {
        this.codigo = codigo;
        this.producto = producto;
        this.imagenFile = imagenFile;
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.estadoRepository = estadoRepository;
        this.blobCreator = blobCreator;
    }

    @Override
    public Producto execute() {
        try {
            Producto productoExistente = validateAndGetProducto();
            updateProductoFields(productoExistente);
            processImagen(productoExistente);
            processCategoria(productoExistente);
            processEstados(productoExistente);
            return saveProducto(productoExistente);
        } catch (IOException e) {
            throw new ValidationException("Error en poder actualizar los datos: " + e.getMessage());
        }
    }

    private Producto validateAndGetProducto() {
        if (codigo <= 0) {
            throw new ValidationException("Código de producto inválido");
        }
        return productoRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    private void updateProductoFields(Producto productoExistente) {
        if (producto.getNombre() != null) {
            productoExistente.setNombre(producto.getNombre());
        }
        if (producto.getPrecio() > 0) {
            productoExistente.setPrecio(producto.getPrecio());
        }
        if (producto.getCreado() != null) {
            productoExistente.setCreado(producto.getCreado());
        }
        if (producto.getTerminado() != null) {
            productoExistente.setTerminado(producto.getTerminado());
        }
        if (producto.getDescripcion() != null) {
            productoExistente.setDescripcion(producto.getDescripcion());
        }
    }

    private void processImagen(Producto productoExistente) throws IOException {
        if (imagenFile != null && !imagenFile.isEmpty()) {
            Blob blob = blobCreator.createBlobFromMultiPartFile(imagenFile);
            productoExistente.setImagen(blob);
        }
    }

    private void processCategoria(Producto productoExistente) {
        if (producto.getCate() != null && producto.getCate().getId() != 0) {
            Categoria categoria = categoriaRepository.findById(producto.getCate().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            productoExistente.setCate(categoria);
        }
    }

    private void processEstados(Producto productoExistente) {
        if (producto.getEstado() != null && !producto.getEstado().isEmpty()) {
            List<Estado> nuevosEstados = new ArrayList<>();
            for (Estado estado : producto.getEstado()) {
                Estado estadoExistente = estadoRepository.findById(estado.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + estado.getTipo()));
                nuevosEstados.add(estadoExistente);
            }
            productoExistente.setEstado(nuevosEstados);
        }
    }

    private Producto saveProducto(Producto producto) {
        try {
            return productoRepository.save(producto);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("Violación de integridad de datos: " + e.getMessage());
        }
    }
}
