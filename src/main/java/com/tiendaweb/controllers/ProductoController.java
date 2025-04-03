package com.tiendaweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendaweb.models.Categoria;
import com.tiendaweb.models.Estado;
import com.tiendaweb.models.Producto;
import com.tiendaweb.repositories.ICategoriaRepository;
import com.tiendaweb.repositories.IEstadoRepository;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.services.IProductoService;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/productos/")
public class ProductoController {

    private IProductoService productService;
    private ICategoriaRepository cateRepo;
    private IEstadoRepository estadoRepo;
    private IProductoRepository prodRepo;

    @Autowired
    public ProductoController(IProductoService productService, ICategoriaRepository cateRepo, IEstadoRepository estadoRepo, IProductoRepository prodRepo) {
        this.productService = productService;
        this.cateRepo = cateRepo;
        this.estadoRepo = estadoRepo;
        this.prodRepo = prodRepo;
    }

    // listamos todos los productos
    @GetMapping("listar")
    public ResponseEntity<?> obtenerTodo() {
        return ResponseEntity.ok(productService.obtenerTodo());
    }

    @PostMapping("create-prod")
    public ResponseEntity<Producto> agregarProducto(@RequestParam("producto") String productoJson, @RequestParam("imagen") MultipartFile imagenFile) throws Exception, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Producto producto = objectMapper.readValue(productoJson, Producto.class);

            // si el producto tiene un ID, intenta cargarlo dede la base de datos para obtener la version
            if(producto.getCodigo() != 0){
                Producto productoExistente = prodRepo.findById(producto.getCodigo())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                producto.setVersion(productoExistente.getVersion()); // copia la version existente
            }

            // guardamos la imagen en el objecto
            if (imagenFile != null && !imagenFile.isEmpty()) {
                Blob blob = createBlobFromMultiPartFile(imagenFile);
                producto.setImagen(blob);
            }

            // agreamos los demas datos de la clase
            producto.setNombre(producto.getNombre());
            producto.setPrecio(producto.getPrecio());
            producto.setCreado(producto.getCreado());
            producto.setTerminado(producto.getTerminado());
            producto.setDescripcion(producto.getDescripcion());
            producto.setEstado(producto.getEstado());

            // agregamos la categoria
            if(producto.getCate() != null && producto.getCate().getId() != 0){
                Categoria categoria = cateRepo.findById(producto.getCate().getId()).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
                producto.setCate(categoria);
            }

            System.out.println("fecha creado formato: " + producto.getCreado());
            System.out.println("fecha terminado formato: " + producto.getTerminado());

            // creamos un estado para el producto
            Estado estado = estadoRepo.findByTipo("Habilitado").get();
            producto.setEstado(Collections.singletonList(estado));

            // guardamos los datos
            Producto prodNuevo = productService.agregarProducto(producto);

            return ResponseEntity.status(HttpStatus.CREATED).body(prodNuevo);
        } catch (StaleObjectStateException e) {
            System.out.println("El producto fue modificado por otra transacción: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            System.out.println("Error al crear el producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private Blob createBlobFromMultiPartFile(MultipartFile file) throws SQLException, IOException {
        byte[] bytes = file.getBytes();
        return new SerialBlob(bytes);
    }

    // actualizar un producto (arreglo, se arreglo pero se tiene que probar)
    @PutMapping("/update-prod/{codigo}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable("codigo") int codigo, @RequestParam("producto") String productoJson, @RequestParam("imagen") MultipartFile imagenFile) throws Exception, IOException, NullPointerException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Producto producto = objectMapper.readValue(productoJson, Producto.class);

            // buscamos el id del producto "en este caso el codigo"
            Producto productoExistente = productService.buscarPorCodigo(codigo).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // actualizamos los campos del producto
            productoExistente.setNombre(producto.getNombre());
            productoExistente.setPrecio(producto.getPrecio());
            productoExistente.setCreado(producto.getCreado());
            productoExistente.setTerminado(producto.getTerminado());
            productoExistente.setDescripcion(producto.getDescripcion());

            // guardamos la imagen en el objecto
            if (imagenFile != null && !imagenFile.isEmpty()) {
                Blob blob = createBlobFromMultiPartFile(imagenFile);
                productoExistente.setImagen(blob);
            }

            // actualizamos la categoria en el producto
            if(producto.getCate().getId() != 0){
                Categoria categoria = cateRepo.findById(producto.getCate().getId()).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
                productoExistente.setCate(categoria);
            }

            // actualizamos el estado de un producto
            if(producto.getEstado() != null && !producto.getEstado().isEmpty()){
                List<Estado> nuevoEstado = new ArrayList<>();
                for(Estado estados : producto.getEstado()){
                    Estado estadoExistente = estadoRepo.findById(estados.getId())
                            .orElseThrow(() -> new RuntimeException("Estado no encontrado: " + estados.getTipo()));
                    nuevoEstado.add(estadoExistente);
                }
                productoExistente.setEstado(nuevoEstado);
            }

            // guardamos los datos del nuevo producto
            Producto productoActualizado = productService.updateProducto(codigo, productoExistente);

            // retornamos los datos guardados
            return ResponseEntity.status(HttpStatus.OK).body(productoActualizado);
        } catch (DataIntegrityViolationException ex) {
            System.out.println("BAD REQUEST (DataIntegrityViolationException): "+ ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            System.out.println("INTERNAL SERVER ERROR (Exception): "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
