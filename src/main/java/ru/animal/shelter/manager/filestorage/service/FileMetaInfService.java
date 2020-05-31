package ru.animal.shelter.manager.filestorage.service;

import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.model.dto.RequestForMultipleFileDTO;
import ru.animal.shelter.manager.filestorage.model.dto.SearchRequestFiles;

import java.util.List;
import java.util.UUID;

public interface FileMetaInfService {

    FileMetaInf getMetaInfFile(UUID userId, UUID fileId);

    List<FileMetaInf> getManyMetaInfFile(RequestForMultipleFileDTO request);

    FileMetaInf saveMetaInfFile(MultipartFile multipartFile, UUID userId, String description);

    FileMetaInf editMetaInfFile(UUID userId, UUID fileId, String description, String fileName);

    void deleteMetaInfFile(UUID fileId);

    List<FileMetaInf> searchMetaInfFile(SearchRequestFiles searchRequestFiles);
}
