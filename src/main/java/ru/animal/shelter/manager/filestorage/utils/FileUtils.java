package ru.animal.shelter.manager.filestorage.utils;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.animal.shelter.manager.filestorage.config.FileStorageProperties;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import javax.validation.ValidationException;
import java.io.File;

@Service
@AllArgsConstructor
public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
    private final FileStorageProperties fileStorageProperties;

    public File getFile(FileMetaInf fileBD, String path, boolean check) {
        var file = new File(path + fileBD.getId());
        if (check) checkFile(file);
        return file;
    }

    public String getPath(FileMetaInf fileBD) {
        var path = fileStorageProperties.getPath() + fileBD.getFilePath() + File.separator;
        checkDirs(path);
        return path;
    }

    private void checkFile(File file) {
        if (!file.exists()) {
            var errMessage = String.format("Файл %s не существует", file.getAbsolutePath());
            throw new ValidationException(errMessage);
        }
    }

    private void checkDirs(String path) {
        var file = new File(path);

        if (!file.exists()) {
            if (file.mkdirs()) {
                LOG.info("Create new catalog: " + file.getPath());
            }
        }
    }
}
