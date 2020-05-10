package ru.animal.shelter.manager.filestorage.service.mappers.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.model.dto.FileMetaInfDTO;
import ru.animal.shelter.manager.filestorage.service.mappers.FileMapper;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileMapperImpl implements FileMapper {

    Clock clock;

//    @Override
//    public FileMetaInf fileDtoToFileMapper(FileMetaInfDTO fileDTO) {
//        return new FileMetaInf(fileDTO.getId(), fileDTO.getFileName(), fileDTO.getFileExt(), fileDTO.getFilePath(),
//                fileDTO.getCreateDate(), fileDTO.getEditDate(), fileDTO.getUserId(), fileDTO.getDescription(),
//                fileDTO.getSize());
//    }

    @Override
    public FileMetaInfDTO fileToFileDtoMapper(FileMetaInf file) {
        return new FileMetaInfDTO(file.getId(), file.getFileName(), file.getFileExt(), file.getCreateDate(),
                file.getEditDate(), file.getUserId(), file.getDescription(), file.getSize());
    }

    @Override
    public FileMetaInf requestSaveFileToFileMetaInfMapper(MultipartFile file, UUID userId, String description) {
        var fullNames = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        var name = fullNames[0];
        var contentType = fullNames[1];
        return new FileMetaInf(null, name, contentType, LocalDate.now(clock.getZone()).toString(),
                LocalDateTime.now(clock.getZone()), LocalDateTime.now(clock.getZone()), userId, description,
                file.getSize());
    }
}
