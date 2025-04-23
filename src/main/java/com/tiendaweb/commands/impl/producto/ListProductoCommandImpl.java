package com.tiendaweb.commands.impl.producto;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Producto;
import com.tiendaweb.repositories.IProductoRepository;

import java.util.List;

public class ListProductoCommandImpl implements Command<List<Producto>> {
    private final IProductoRepository iProductoRepository;

    public ListProductoCommandImpl(IProductoRepository iProductoRepository) {
        this.iProductoRepository = iProductoRepository;
    }

    @Override
    public List<Producto> execute() {
        return iProductoRepository.findAll();
    }
}
