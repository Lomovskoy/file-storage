package ru.animal.shelter.manager.filestorage.config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Getter
@Configuration
public class FileStorageProperties {

    private static final Logger LOG = LoggerFactory.getLogger(FileStorageProperties.class);
    private final String path;
    private final long maxUploadSizeFile;
    private final int maxNumberUploadSizeFile;
    private final int maxNumberDownloadedSizeFile;
    private final int compression;
    private final int numberOfUploadedFiles;

    public FileStorageProperties(@Value("${file-storage.path}") String path,
                                 @Value("${file-storage.max-upload-size-file}") long maxUploadSizeFile,
                                 @Value("${file-storage.max-number-upload-files}") int maxNumberUploadSizeFile,
                                 @Value("${file-storage.max-number-downloaded-files}") int maxNumberDownloadedSizeFile,
                                 @Value("${file-storage.compression-files}") int compression,
                                 @Value("${file-storage.number-of-uploaded-files}") int numberOfUploadedFiles) {
        this.path = getPath(path);
        this.maxUploadSizeFile = maxUploadSizeFile;
        this.maxNumberUploadSizeFile = maxNumberUploadSizeFile;
        this.maxNumberDownloadedSizeFile = maxNumberDownloadedSizeFile;
        this.compression = compression;
        this.numberOfUploadedFiles = numberOfUploadedFiles;
        log();
    }

    private String getPath(String path){
        return path.replace("/", File.separator).replace("\\", File.separator);
    }

    private void log() {
        LOG.info(String.format("File Storage config: path = %s maxUploadSizeFile = %s maxNumberUploadSizeFile= %s " +
                        "maxNumberDownloadedSizeFile = %s compression = %s numberOfUploadedFiles = %s",
                path, maxUploadSizeFile, maxNumberUploadSizeFile, maxNumberDownloadedSizeFile,
                compression, numberOfUploadedFiles));
    }
}
