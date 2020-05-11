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
    private final long maxUploadSize;

    public FileStorageProperties(@Value("${file-storage.path}") String path,
                                 @Value("${file-storage.max-upload-size-file}") int maxUploadSizeFile,
                                 @Value("${file-storage.max-upload-size}") int maxUploadSize) {
        this.path = path;
        this.maxUploadSizeFile = maxUploadSizeFile;
        this.maxUploadSize = maxUploadSize;
        log(path, maxUploadSizeFile, maxUploadSize);
    }

    private void log(String path, int maxUploadSizeFile, int maxUploadSize) {
        LOG.info(String.format("File Storage config: path = %s maxUploadSizeFile = %s maxUploadSize = %s",
                path, maxUploadSizeFile, maxUploadSize));
    }
}
