package com.tiendaweb.commands.impl.tarea.utils;

import org.springframework.web.multipart.MultipartFile;
import java.sql.Blob;
import java.io.IOException;

public interface BlobCreator {
    Blob createBlobFromMultiPartFile(MultipartFile file) throws IOException;
}
