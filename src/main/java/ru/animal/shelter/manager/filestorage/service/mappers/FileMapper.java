package ru.animal.shelter.manager.filestorage.service.mappers;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.model.dto.FileMetaInfDTO;
import java.util.UUID;

@Service
public interface FileMapper {

//    FileMetaInf fileDtoToFileMapper(FileMetaInfDTO fileDTO);

    FileMetaInfDTO fileToFileDtoMapper(FileMetaInf file);

    FileMetaInf requestSaveFileToFileMetaInfMapper(MultipartFile file, UUID userId, String description);
}
