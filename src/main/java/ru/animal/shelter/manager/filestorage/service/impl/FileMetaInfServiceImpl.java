package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.model.dto.RequestForMultipleFileDTO;
import ru.animal.shelter.manager.filestorage.repository.FileRepository;
import ru.animal.shelter.manager.filestorage.service.FileMetaInfService;
import ru.animal.shelter.manager.filestorage.service.mappers.impl.FileMapperImpl;
import javax.validation.ValidationException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileMetaInfServiceImpl implements FileMetaInfService {

    private static final Logger LOG = LoggerFactory.getLogger(FileMetaInfServiceImpl.class);

    private final Clock clock;
    private final FileMapperImpl fileMapper;
    private final FileRepository fileRepository;

    @Override
    public FileMetaInf getMetaInfFile(UUID userId, UUID fileId) {
        var fileMetaInf = fileRepository.findById(fileId).orElseThrow();
        validationOfRights(userId, fileId, fileMetaInf);
        return fileMetaInf;
    }

    @Override
    public List<FileMetaInf> getManyMetaInfFile(RequestForMultipleFileDTO request) {
        var fileMetaInfList = fileRepository.findAllById(request.getFileId());
        return fileMetaInfList.stream().filter(f-> f.getUserId().equals(request.getUserId())).collect(Collectors.toList());
    }

    @Override
    public FileMetaInf saveMetaInfFile(MultipartFile multipartFile, UUID userId, String description) {
        var fileMetaInf = buildFileMetaInf(multipartFile, userId, description);
        var fileMetaInfDB = fileRepository.save(fileMetaInf);
        LOG.info("Meta-information about the file was successfully saved to the database");
        return fileMetaInfDB;
    }

    @Override
    public FileMetaInf editMetaInfFile(UUID userId, UUID fileId, String description) {
        var fileMetaInf = getMetaInfFile(userId, fileId);
        validationOfRights(userId, fileId, fileMetaInf);

        if (!description.isEmpty() || !description.equals("")){
            fileMetaInf.setDescription(description);
            fileMetaInf.setEditDate(LocalDateTime.now(clock.getZone()));
            fileRepository.save(fileMetaInf);
            log(userId, fileId);
        }
        return fileMetaInf;
    }

    @Override
    public void deleteMetaInfFile(UUID fileId) {
        fileRepository.deleteById(fileId);
    }

    private FileMetaInf buildFileMetaInf(MultipartFile multipartFile, UUID userId, String description) {
        return fileMapper.requestSaveFileToFileMetaInfMapper(multipartFile, userId, description);
    }

    private void log(UUID userId, UUID fileId) {
        LOG.info(String.format("Meta-information about the file %s, was changed by the user %s, " +
                "and saved to the database successfully", fileId, userId));
    }

    private void validationOfRights(UUID userId, UUID fileId, FileMetaInf fileMetaInf) {
        if (!fileMetaInf.getId().equals(fileId) || !fileMetaInf.getUserId().equals(userId))
            throw new ValidationException(String.format("У пользователя %s нет прав на файл %s ", userId, fileId));
    }
}
