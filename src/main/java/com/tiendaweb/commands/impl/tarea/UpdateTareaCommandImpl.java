package com.tiendaweb.commands.impl.tarea;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.impl.tarea.utils.BlobCreator;
import com.tiendaweb.models.Producto;
import com.tiendaweb.models.Tarea;
import com.tiendaweb.repositories.IProductoRepository;
import com.tiendaweb.repositories.ITareaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;

public class UpdateTareaCommandImpl implements Command<Tarea> {
    private final Long id;
    private final Tarea tarea;
    private final MultipartFile videoFile;
    private final ITareaRepository iTareaRepository;
    private final IProductoRepository iProductoRepository;
    private final BlobCreator blobCreator;

    public UpdateTareaCommandImpl(Long id, Tarea tarea, MultipartFile videoFile, ITareaRepository iTareaRepository, IProductoRepository iProductoRepository, BlobCreator blobCreator) {
        this.id = id;
        this.tarea = tarea;
        this.videoFile = videoFile;
        this.iTareaRepository = iTareaRepository;
        this.iProductoRepository = iProductoRepository;
        this.blobCreator = blobCreator;
    }

    @Override
    public Tarea execute() {
        try {
            Tarea tareaExistente = validateAndGetTarea();
            updateTareaFields(tareaExistente);
            processVideo(tareaExistente);
            processProducto(tareaExistente);
            return saveTarea(tareaExistente);
        }catch (IOException e) {
            throw new IllegalAccessError("Error al actualizar los datos de una tarea" + e);
        }
    }

    private Tarea validateAndGetTarea() {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de tarea inválido");
        }
        return iTareaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
    }

    private void updateTareaFields(Tarea tareaExistente) {
        if (tarea.getNombre() != null) {
            tareaExistente.setNombre(tarea.getNombre());
        }
    }

    private void processVideo(Tarea tareaExistente) throws IOException {
        if (videoFile != null && !videoFile.isEmpty()) {
            Blob blob = blobCreator.createBlobFromMultiPartFile(videoFile);
            tareaExistente.setVideo(blob);
        }
    }

    private void processProducto(Tarea tareaExistente) {
        if (tarea.getProducto() != null && tarea.getProducto().getCodigo() != 0) {
            Producto producto = iProductoRepository.findById(tarea.getProducto().getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            tareaExistente.setProducto(producto);
        }
    }

    private Tarea saveTarea(Tarea tarea) {
        return iTareaRepository.save(tarea);
    }
}
