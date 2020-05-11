package ru.animal.shelter.manager.filestorage.service;

import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;

import java.util.UUID;

public interface FileMetaInfService {

    FileMetaInf getMetaInfFile(UUID userId, UUID fileId);

    FileMetaInf saveMetaInfFile(MultipartFile multipartFile, UUID userId, String description);

    FileMetaInf editMetaInfFile(UUID userId, UUID fileId, String description);

    void deleteMetaInfFile(UUID fileId);
}
