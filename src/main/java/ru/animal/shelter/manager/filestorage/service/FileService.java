package ru.animal.shelter.manager.filestorage.service;

import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;

import java.util.UUID;

public interface FileService {

    FileMetaInf getFile(UUID fileId);

    FileMetaInf saveFile(MultipartFile multipartFile, UUID userId, String description);

    FileMetaInf editFile(FileMetaInf multipartFile);

    void deleteFile(UUID fileId);
}
