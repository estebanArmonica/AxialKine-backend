package com.tiendaweb.commands.impl.tarea.impl;

import com.tiendaweb.commands.impl.tarea.utils.BlobCreator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Component
public class BlobCreatorImpl implements BlobCreator {

    @Override
    public Blob createBlobFromMultiPartFile(MultipartFile file) throws IOException {
        try {
            return new SerialBlob(file.getBytes());
        }catch (SQLException e) {
            throw new IOException("Error al crear Blob desde archivo", e);
        }
    }
}
