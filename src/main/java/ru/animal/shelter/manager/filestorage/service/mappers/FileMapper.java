package ru.animal.shelter.manager.filestorage.service.mappers;

import org.springframework.stereotype.Service;
import ru.animal.shelter.manager.filestorage.model.File;
import ru.animal.shelter.manager.filestorage.model.dto.FileDTO;

@Service
public interface FileMapper {

    File fileDtoToFileMapper(FileDTO fileDTO);

    FileDTO fileToFileDtoMapper(File file);
}
