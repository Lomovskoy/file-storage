package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.config.FileStorageProperties;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.repository.FileRepository;
import ru.animal.shelter.manager.filestorage.service.FileService;
import ru.animal.shelter.manager.filestorage.service.mappers.impl.FileMapperImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    FileMapperImpl fileMapper;
    FileRepository fileRepository;
    FileStorageProperties fileStorageProperties;

    @Override
    public FileMetaInf getFile(UUID fileId) {
        return fileRepository.findById(fileId).orElse(new FileMetaInf());
    }

    @Override
    public FileMetaInf saveFile(MultipartFile multipartFile, UUID userId, String description) {
        var fileMetaInf = fileMapper.requestSaveFileToFileMetaInfMapper(multipartFile, userId, description);
        var fileBD = fileRepository.save(fileMetaInf);
        var path = fileStorageProperties.getPath() + File.separator + fileBD.getFilePath();
        var file = new File(path);
        if (!file.exists()) file.mkdirs();

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(path + File.separator + fileBD.getId()))){
            FileCopyUtils.copy(multipartFile.getBytes(), fileOutputStream);
        } catch (IOException ex){
            LOG.error(ex.getMessage());
        }
        return fileBD;
    }

    @Override
    public FileMetaInf editFile(FileMetaInf file) {
        return fileRepository.save(file);
    }

    @Override
    public void deleteFile(UUID fileId) {
        fileRepository.deleteById(fileId);
    }
}
