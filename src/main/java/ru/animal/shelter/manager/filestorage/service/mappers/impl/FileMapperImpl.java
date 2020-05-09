package ru.animal.shelter.manager.filestorage.service.mappers.impl;

import org.springframework.stereotype.Service;
import ru.animal.shelter.manager.filestorage.model.File;
import ru.animal.shelter.manager.filestorage.model.dto.FileDTO;
import ru.animal.shelter.manager.filestorage.service.mappers.FileMapper;

@Service
public class FileMapperImpl implements FileMapper {

    @Override
    public File fileDtoToFileMapper(FileDTO fileDTO) {
        return new File(fileDTO.getId(), fileDTO.getFileName(), fileDTO.getFileExt(), fileDTO.getFilePath(),
                fileDTO.getCreateDate(), fileDTO.getEditDate(), fileDTO.getUserId(), fileDTO.getSize());
    }

    @Override
    public FileDTO fileToFileDtoMapper(File file) {
        return new FileDTO(file.getId(), file.getFileName(), file.getFileExt(), file.getFilePath(),
                file.getCreateDate(), file.getEditDate(), file.getUserId(), file.getSize());
    }
}
