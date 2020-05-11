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
    private final int maxNumberUploadSizeFile;
    private final int maxNumberDownloadedSizeFile;

    public FileStorageProperties(@Value("${file-storage.path}") String path,
                                 @Value("${file-storage.max-upload-size-file}") long maxUploadSizeFile,
                                 @Value("${file-storage.max-number-upload-files}") int maxNumberUploadSizeFile,
                                 @Value("${file-storage.max-number-downloaded-files}") int maxNumberDownloadedSizeFile) {
        this.path = path;
        this.maxUploadSizeFile = maxUploadSizeFile;
        this.maxNumberUploadSizeFile = maxNumberUploadSizeFile;
        this.maxNumberDownloadedSizeFile = maxNumberDownloadedSizeFile;
        log();
    }

    private void log() {
        LOG.info(String.format("File Storage config: path = %s maxUploadSizeFile = %s maxNumberUploadSizeFile= %s " +
                        "maxNumberDownloadedSizeFile = %s",
                path, maxUploadSizeFile, maxNumberUploadSizeFile, maxNumberDownloadedSizeFile));
    }
}
