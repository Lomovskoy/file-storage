package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.model.dto.RequestForMultipleFileDTO;
import ru.animal.shelter.manager.filestorage.model.dto.SearchRequestFiles;
import ru.animal.shelter.manager.filestorage.repository.FileRepository;
import ru.animal.shelter.manager.filestorage.service.FileMetaInfService;
import ru.animal.shelter.manager.filestorage.service.FileSearchSpecification;
import ru.animal.shelter.manager.filestorage.service.mappers.impl.FileMapperImpl;
import javax.validation.ValidationException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileMetaInfServiceImpl implements FileMetaInfService {

    private static final Logger LOG = LoggerFactory.getLogger(FileMetaInfServiceImpl.class);

    private final Clock clock;
    private final FileMapperImpl fileMapper;
    private final FileRepository fileRepository;
    private final FileSearchSpecification fileSearchSpecification;

    @Override
    public FileMetaInf getMetaInfFile(UUID userId, UUID fileId) {
        var fileMetaInf = fileRepository.findById(fileId).orElseThrow();
        validationOfRights(userId, fileId, fileMetaInf);
        return fileMetaInf;
    }

    @Override
    public List<FileMetaInf> getManyMetaInfFile(RequestForMultipleFileDTO request) {
        return fileRepository.findByIdInAndUserId(request.getFileId(), request.getUserId());
    }

    @Override
    public FileMetaInf saveMetaInfFile(MultipartFile multipartFile, UUID userId, String description) {
        var fileMetaInf = buildFileMetaInf(multipartFile, userId, description);
        var fileMetaInfDB = fileRepository.save(fileMetaInf);
        LOG.info("Meta-information about the file was successfully saved to the database");
        return fileMetaInfDB;
    }

    @Override
    public FileMetaInf editMetaInfFile(UUID userId, UUID fileId, String description, String fileName) {
        var fileMetaInf = getMetaInfFile(userId, fileId);

        if (description != null){
            var descriptionTrim = description.trim();
            if (!descriptionTrim.isEmpty()){
                editDescription(userId, fileId, descriptionTrim, fileMetaInf);
                LOG.info("Description about the file was successfully edit to the database");
            }
        }
        if (fileName != null){
            var fileNameTrim = fileName.trim();
            if (!fileNameTrim.isEmpty()){
                editFileName(userId, fileId, fileNameTrim, fileMetaInf);
                LOG.info("FileName about the file was successfully edit to the database");
            }
        }
        return fileMetaInf;
    }

    @Override
    public void deleteMetaInfFile(UUID fileId) {
        var fileMetaInf = fileRepository.findById(fileId).orElseThrow();
        fileRepository.deleteById(fileMetaInf.getId());
    }

    @Override
    public Page<FileMetaInf> searchMetaInfFile(SearchRequestFiles searchRequestFiles) {
        var page = fileSearchSpecification.getPage(searchRequestFiles);
        var specification = fileSearchSpecification.getSpecificationForFilters(searchRequestFiles);
        return fileRepository.findAll(specification, page);
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

    private void editFileName(UUID userId, UUID fileId, String fileName, FileMetaInf fileMetaInf) {
        fileMetaInf.setFileName(fileName);
        fileMetaInf.setEditDate(LocalDateTime.now(clock.getZone()));
        fileRepository.save(fileMetaInf);
        log(userId, fileId);
    }

    private void editDescription(UUID userId, UUID fileId, String description, FileMetaInf fileMetaInf) {
        fileMetaInf.setDescription(description);
        fileMetaInf.setEditDate(LocalDateTime.now(clock.getZone()));
        fileRepository.save(fileMetaInf);
        log(userId, fileId);
    }
}
