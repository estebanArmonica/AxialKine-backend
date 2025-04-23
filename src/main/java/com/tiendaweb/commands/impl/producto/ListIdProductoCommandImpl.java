package com.tiendaweb.commands.impl.producto;

import com.tiendaweb.commands.Command;
import com.tiendaweb.models.Producto;
import com.tiendaweb.repositories.IProductoRepository;

import java.util.Optional;

public class ListIdProductoCommandImpl implements Command<Optional<Producto>> {
    private final int codigo;
    private final IProductoRepository iProductoRepository;

    public ListIdProductoCommandImpl(int codigo, IProductoRepository iProductoRepository) {
        this.codigo = codigo;
        this.iProductoRepository = iProductoRepository;
    }

    public int getCodigo() {
        return codigo;
    }

    @Override
    public Optional<Producto> execute() {
        if(codigo == 0 || codigo <= 0){
            throw new UnsupportedOperationException("CODIGO de producto invalido");
        }
        return iProductoRepository.findById(codigo);
    }
}
