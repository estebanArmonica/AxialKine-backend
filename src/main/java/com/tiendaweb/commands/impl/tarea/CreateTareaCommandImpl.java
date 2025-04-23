package com.tiendaweb.commands.impl.tarea;

import com.tiendaweb.commands.Command;
import com.tiendaweb.commands.impl.tarea.utils.BlobCreator;
import com.tiendaweb.models.Producto;
import com.tiendaweb.models.Tarea;
import com.tiendaweb.repositories.IProductoRepository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;

public class CreateTareaCommandImpl implements Command<Tarea> {
    private final Tarea tarea;
    private final MultipartFile video;
    private final IProductoRepository iProductoRepository;
    private final BlobCreator blobCreator;

    public CreateTareaCommandImpl(Tarea tarea, MultipartFile video, IProductoRepository iProductoRepository, BlobCreator blobCreator) {
        this.tarea = tarea;
        this.video = video;
        this.iProductoRepository = iProductoRepository;
        this.blobCreator = blobCreator;
    }

    @Override
    public Tarea execute() {
        try {
            validateTarea();
            processVideo();
            processProducto();
            return tarea;
        } catch (IOException e) {
            return null;
        }
    }

    private void validateTarea() {
        if (tarea.getNombre() == null || tarea.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la tarea es requerido");
        }
    }

    private void processVideo() throws IOException {
        if (video != null && !video.isEmpty()) {
            Blob blob = blobCreator.createBlobFromMultiPartFile(video);
            tarea.setVideo(blob);
        }
    }

    private void processProducto() {
        if (tarea.getProducto() != null && tarea.getProducto().getCodigo() != 0) {
            Producto producto = iProductoRepository.findById(tarea.getProducto().getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            tarea.setProducto(producto);
        }
    }
}
