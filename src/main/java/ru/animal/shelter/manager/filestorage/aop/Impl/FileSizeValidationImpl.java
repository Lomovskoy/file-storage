package ru.animal.shelter.manager.filestorage.aop.Impl;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.animal.shelter.manager.filestorage.aop.Validation;
import ru.animal.shelter.manager.filestorage.config.FileStorageProperties;

@Aspect
@Component
@AllArgsConstructor
public class FileSizeValidationImpl extends Validation {

    private static final String MULTIPART_FILE = "multipartFile";

    FileStorageProperties fileStorageProperties;

    @Before("@annotation(ru.animal.shelter.manager.filestorage.aop.FileSizeValidation)")
    public void fullVerificationOfRole(JoinPoint point) {
        String[] names = getNames(point);
        checkName(names, MULTIPART_FILE);
        var file = getValueMultipart(point, names, MULTIPART_FILE);
        if (file.getSize() > fileStorageProperties.getMaxUploadSizeFile()){
            throw new MaxUploadSizeExceededException(fileStorageProperties.getMaxUploadSizeFile());
        }
    }

}
