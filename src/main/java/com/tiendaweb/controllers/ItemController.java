package com.tiendaweb.controllers;

import com.tiendaweb.models.Item;
import com.tiendaweb.models.Producto;
import com.tiendaweb.models.Usuario;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.repositories.IUsuarioRepository;
import com.tiendaweb.services.IItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/items/")
@CrossOrigin(origins = {"http://localhost:4200"},
        methods = {RequestMethod.GET,RequestMethod.POST, RequestMethod.PUT})
public class ItemController {

    private IItemService itemService;
    private IProductoRepository prodRepo;
    private IUsuarioRepository userRepo;

    @Autowired
    public ItemController(IItemService itemService, IProductoRepository prodRepo, IUsuarioRepository userRepo) {
        this.itemService = itemService;
        this.prodRepo = prodRepo;
        this.userRepo = userRepo;
    }

    // listamo todo del item de un usuario
    @GetMapping("listar")
    public ResponseEntity<?> obtenerTodoLista() {
        return ResponseEntity.ok(itemService.obtenerTodaLista());
    }

    // listamos solo un item del producto por usuario
    @GetMapping("listar/{id}")
    public ResponseEntity<?> obtenerPorIdItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemService.buscarPorIdItem(id));
    }

    // agregamos un producto al item
    @PostMapping("crear-item")
    public ResponseEntity<Item> agregarItem(@RequestBody Item item) throws Exception {
        try {
            System.out.println("Item recibido: " + item);

            if (item.getProducto() != null && item.getProducto().getCodigo() != 0) {
                Producto productoExiste = prodRepo.findById(item.getProducto().getCodigo())
                        .orElseThrow(() -> new RuntimeException("El producto no existe o no fue proporcionado"));
                item.setProducto(productoExiste);
            } else {
                throw new RuntimeException("El producto debe ser proporcionado y su código debe ser un valor positivo");
            }

            if(item.getUser() != null && item.getUser().getId() != null){
                Usuario usuarioExiste = userRepo.findById(item.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("El usuario no existe o no esta creado"));
                item.setUser(usuarioExiste);
            } else {
                throw new RuntimeException("El usuario debe estar proporcionado y tener un ID válido");
            }

            item.setCantidad(item.getCantidad());
            item.setPrecio(item.getPrecio());

            // log para ver si producto lo encuentra
            System.out.println("Producto ID: " + item.getProducto().getCodigo());

            // guardamos los datos
            Item itemNuevo = itemService.agregarItem(item);

            return ResponseEntity.status(HttpStatus.CREATED).body(itemNuevo);
        } catch (Exception e) {
            System.out.println("Error al crear un item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("update-item/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable("id") Long id, @RequestBody Item item) throws Exception {

        // buscamos primero si existe el id del item
        Item itemExiste = itemService.buscarPorIdItem(id).orElseThrow(() -> new RuntimeException("El item no existe"));

        // actualizamos los datos del item
        itemExiste.setCantidad(0);
        itemExiste.setPrecio(0);

        itemExiste.setProducto(null);

        // guardamos los datos del item nuevo
        Item actualizarItem = itemService.updateItem(id, itemExiste);

        return ResponseEntity.ok(actualizarItem);
    }
}
