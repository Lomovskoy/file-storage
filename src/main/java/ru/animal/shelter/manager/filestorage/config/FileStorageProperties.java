package ru.animal.shelter.manager.filestorage.config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class FileStorageProperties {

    private static final Logger LOG = LoggerFactory.getLogger(FileStorageProperties.class);
    private final String path;
    private final long maxUploadSizeFile;

    public FileStorageProperties(@Value("${file-storage.path}") String path,
                                 @Value("${file-storage.max-upload-size-file}") long maxUploadSizeFile) {
        this.path = path;
        this.maxUploadSizeFile = maxUploadSizeFile;
        log(path, maxUploadSizeFile);
    }

    private void log(String path, long maxUploadSizeFile) {
        LOG.info(String.format("File Storage config: path = %s maxUploadSizeFile = %s ", path, maxUploadSizeFile));
    }
}
