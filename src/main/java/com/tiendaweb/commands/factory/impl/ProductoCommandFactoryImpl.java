package com.tiendaweb.commands.factory.impl;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.factory.ProductoCommandFactory;
import com.tiendaweb.commands.impl.producto.CreateProductoCommandImpl;
import com.tiendaweb.commands.impl.producto.ListIdProductoCommandImpl;
import com.tiendaweb.commands.impl.producto.ListProductoCommandImpl;
import com.tiendaweb.commands.impl.producto.UpdateProductoCommandImpl;
import com.tiendaweb.commands.impl.tarea.utils.BlobCreator;
import com.tiendaweb.models.Producto;
import com.tiendaweb.repositories.ICategoriaRepository;
import com.tiendaweb.repositories.IEstadoRepository;
import com.tiendaweb.repositories.IProductoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Component
public class ProductoCommandFactoryImpl implements ProductoCommandFactory {
    private final IProductoRepository iProductoRepository;
    private final ICategoriaRepository iCategoriaRepository;
    private final IEstadoRepository iEstadoRepository;
    private final BlobCreator blobCreator;

    public ProductoCommandFactoryImpl(IProductoRepository iProductoRepository, ICategoriaRepository iCategoriaRepository, IEstadoRepository iEstadoRepository, BlobCreator blobCreator) {
        this.iProductoRepository = iProductoRepository;
        this.iCategoriaRepository = iCategoriaRepository;
        this.iEstadoRepository = iEstadoRepository;
        this.blobCreator = blobCreator;
    }

    // creamos un producto
    @Override
    public Command<Producto> createProductoCommand(Producto producto, MultipartFile imagenFile) {
        return new CreateProductoCommandImpl(
                producto,
                imagenFile,
                iProductoRepository,
                iCategoriaRepository,
                iEstadoRepository,
                blobCreator
        );
    }

    // actualizamos un producto buscando a travez de su id
    @Override
    public Command<Producto> updateProductoCommand(int codigo, Producto producto, MultipartFile imagenFile) {
        return new UpdateProductoCommandImpl(
                codigo,
                producto,
                imagenFile,
                iProductoRepository,
                iCategoriaRepository,
                iEstadoRepository,
                blobCreator
        );
    }

    @Override
    public Command<List<Producto>> obtenerTodoProductoCommand() {
        return new ListProductoCommandImpl(iProductoRepository);
    }

    @Override
    public Command<Optional<Producto>> buscarProductoCommand(int codigo) {
        return new ListIdProductoCommandImpl(codigo, iProductoRepository);
    }
}
