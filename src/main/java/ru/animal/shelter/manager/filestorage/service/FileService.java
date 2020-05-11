package ru.animal.shelter.manager.filestorage.service;

import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public interface FileService {

    void getFile(UUID fileId, HttpServletResponse response) throws IOException;

    FileMetaInf saveFile(MultipartFile multipartFile, UUID userId, String description) throws IOException;

    void deleteFile(UUID fileId) throws IOException;
}
