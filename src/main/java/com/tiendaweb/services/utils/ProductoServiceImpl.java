package com.tiendaweb.services.utils;

import com.tiendaweb.models.Producto;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.services.IProductoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

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
    public Optional<Producto> buscarPorCodigo(int codigo) {
        return productRepo.findByCodigo(codigo);
    }
}
