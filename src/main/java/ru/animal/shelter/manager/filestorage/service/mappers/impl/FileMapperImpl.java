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
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileMapperImpl implements FileMapper {

    private final Clock clock;

    @Override
    public FileMetaInfDTO fileToFileDtoMapper(FileMetaInf file) {
        return new FileMetaInfDTO(file.getId(), file.getFileName(), file.getFileExt(), file.getCreateDate(),
                file.getEditDate(), file.getUserId(), file.getDescription(), file.getSize());
    }

    @Override
    public FileMetaInf requestSaveFileToFileMetaInfMapper(MultipartFile file, UUID userId, String description) {
        var fullNames = Arrays.asList(Objects.requireNonNull(file.getOriginalFilename()).split("\\."));
        var contentType = fullNames.get(fullNames.size() - 1);
        var fullName = file.getOriginalFilename().replace("." + contentType, "");
        return new FileMetaInf(null, fullName, contentType, LocalDate.now(clock.getZone()).toString(),
                LocalDateTime.now(clock.getZone()), LocalDateTime.now(clock.getZone()), userId, description,
                file.getSize());
    }
}
