package com.tiendaweb.commands.impl.producto;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.impl.tarea.utils.BlobCreator;
import com.tiendaweb.exception.ConcurrencyException;
import com.tiendaweb.exception.ResourceNotFoundException;
import com.tiendaweb.exception.ValidationException;
import com.tiendaweb.models.Categoria;
import com.tiendaweb.models.Estado;
import com.tiendaweb.models.Producto;

import com.tiendaweb.repositories.ICategoriaRepository;
import com.tiendaweb.repositories.IEstadoRepository;
import com.tiendaweb.repositories.IProductoRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.util.Collections;
public class CreateProductoCommandImpl implements Command<Producto> {
    private final Producto producto;
    private final MultipartFile imagenFile;
    private final IProductoRepository iProductoRepository;
    private final ICategoriaRepository iCategoriaRepository;
    private final IEstadoRepository iEstadoRepository;
    private final BlobCreator blobCreator;

    public CreateProductoCommandImpl(Producto producto, MultipartFile imagenFile, IProductoRepository iProductoRepository, ICategoriaRepository iCategoriaRepository, IEstadoRepository iEstadoRepository, BlobCreator blobCreator) {
        this.producto = producto;
        this.imagenFile = imagenFile;
        this.iProductoRepository = iProductoRepository;
        this.iCategoriaRepository = iCategoriaRepository;
        this.iEstadoRepository = iEstadoRepository;
        this.blobCreator = blobCreator;
    }

    @Override
    public Producto execute() {
        try {
            validateProducto();
            handleExistingProducto();
            processImagen();
            processCategoria();
            setEstadoDefault();
            return saveProducto();
        }catch (IOException e){
            throw new ValidationException("Error al cargas los datos: " + e.getMessage());
        }
    }

    // válidamos el producto
    private void validateProducto() {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new ValidationException("El nombre del producto es requerido");
        }
        if (producto.getPrecio() <= 0) {
            throw new ValidationException("El precio debe ser mayor que cero");
        }
    }

    // creamos un handle para verificar si el producto existe
    private void handleExistingProducto() {
        if (producto.getCodigo() != 0) {
            Producto existente = iProductoRepository.findById(producto.getCodigo())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
            producto.setVersion(existente.getVersion());
        }
    }

    // procesamos las imagens de tipo blob para ser transformados a byte[]
    private void processImagen() throws IOException {
        if (imagenFile != null && !imagenFile.isEmpty()) {
            Blob blob = blobCreator.createBlobFromMultiPartFile(imagenFile);
            producto.setImagen(blob);
        }
    }

    /*
    * procesamos la categoria, la buscamos primero por ID
    * para verificar que exista antes de agregarlo a un producto
    * asi mismo lo realizamos con los estados
    */
    private void processCategoria() {
        if (producto.getCate() != null && producto.getCate().getId() != 0) {
            Categoria categoria = iCategoriaRepository.findById(producto.getCate().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            producto.setCate(categoria);
        }
    }

    private void setEstadoDefault() {
        Estado estado = iEstadoRepository.findByTipo("Habilitado")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Habilitado' no encontrado"));
        producto.setEstado(Collections.singletonList(estado));
    }

    // guardamos los datos para crear el producto
    private Producto saveProducto() {
        try {
            return iProductoRepository.save(producto);
        } catch (StaleObjectStateException e) {
            throw new ConcurrencyException("El producto fue modificado por otra transacción");
        }
    }
}
