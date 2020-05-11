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
import ru.animal.shelter.manager.filestorage.service.FileMetaInfService;
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

    FileMetaInfService fileMetaInfService;
    FileStorageProperties fileStorageProperties;

    @Override
    public FileMetaInf getFile(UUID fileId) {
        return null;
    }

    @Override
    public FileMetaInf saveFile(MultipartFile multipartFile, UUID userId, String description) {
        var fileBD = fileMetaInfService.saveMetaInfFile(multipartFile, userId, description);
        var path = getPath(fileBD);
        checkDirs(path);

        try (var fileOutputStream = new FileOutputStream(getFile(fileBD, path))){
            FileCopyUtils.copy(multipartFile.getBytes(), fileOutputStream);
            LOG.info("File successfully written to disk");
        } catch (IOException ex){
            LOG.info("Error writing file to disk: " + ex);
        }
        return fileBD;
    }

    @Override
    public void deleteFile(UUID fileId) {

    }

    private String getPath(FileMetaInf fileBD) {
        return fileStorageProperties.getPath() + File.separator + fileBD.getFilePath();
    }

    private void checkDirs(String path) {
        var file = new File(path);

        if (!file.exists())
            if (file.mkdirs())
                LOG.info("Create new catalog: " + file.getPath());
    }

    private File getFile(FileMetaInf fileBD, String path) {
        return new File(path + File.separator + fileBD.getId());
    }
}
