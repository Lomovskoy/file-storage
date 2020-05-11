package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.repository.FileRepository;
import ru.animal.shelter.manager.filestorage.service.FileMetaInfService;
import ru.animal.shelter.manager.filestorage.service.mappers.impl.FileMapperImpl;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileMetaInfServiceImpl implements FileMetaInfService {

    private static final Logger LOG = LoggerFactory.getLogger(FileMetaInfServiceImpl.class);

    FileMapperImpl fileMapper;
    FileRepository fileRepository;

    @Override
    public FileMetaInf getMetaInfFile(UUID fileId) {
        return fileRepository.findById(fileId).orElseThrow();
    }

    @Override
    public FileMetaInf saveMetaInfFile(MultipartFile multipartFile, UUID userId, String description) {
        var fileMetaInf = getFileMetaInf(multipartFile, userId, description);
        var fileMetaInfDB = fileRepository.save(fileMetaInf);
        LOG.info("Meta-information about the file was successfully saved to the database");
        return fileMetaInfDB;
    }

    @Override
    public FileMetaInf editMetaInfFile(FileMetaInf multipartFile) {
        return null;
    }

    @Override
    public void deleteMetaInfFile(UUID fileId) {
        fileRepository.deleteById(fileId);
    }

    private FileMetaInf getFileMetaInf(MultipartFile multipartFile, UUID userId, String description) {
        return fileMapper.requestSaveFileToFileMetaInfMapper(multipartFile, userId, description);
    }
}
