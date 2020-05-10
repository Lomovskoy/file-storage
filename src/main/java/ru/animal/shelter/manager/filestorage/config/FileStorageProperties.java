package ru.animal.shelter.manager.filestorage.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class FileStorageProperties {

    String path;

    long maxUploadSizePerFile;

    long maxUploadSize;

    public FileStorageProperties(@Value("${file-storage.path}") String path,
                                 @Value("${file-storage.max-upload-size-file}") int maxUploadSizePerFile,
                                 @Value("${file-storage.max-upload-size}") int maxUploadSize) {
        this.path = path;
        this.maxUploadSizePerFile = maxUploadSizePerFile;
        this.maxUploadSize = maxUploadSize;
    }
}
