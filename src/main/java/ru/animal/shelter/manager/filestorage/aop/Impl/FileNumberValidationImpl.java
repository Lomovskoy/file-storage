package ru.animal.shelter.manager.filestorage.aop.Impl;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.animal.shelter.manager.filestorage.aop.Validation;
import ru.animal.shelter.manager.filestorage.config.FileStorageProperties;

import javax.validation.ValidationException;

@Aspect
@Component
@AllArgsConstructor
public class FileNumberValidationImpl extends Validation {

    private static final String MULTIPART_FILE_DTO = "requestForMultipleFile";

    FileStorageProperties fileStorageProperties;

    @Before("@annotation(ru.animal.shelter.manager.filestorage.aop.FileNumberValidation)")
    public void fullVerificationOfRole(JoinPoint point) {
        String[] names = getNames(point);
        checkName(names, MULTIPART_FILE_DTO);
        var file = getValueFileNumber(point, names, MULTIPART_FILE_DTO);
        if (file.getFileId().size() > fileStorageProperties.getNumberOfUploadedFiles()){
            throw new ValidationException("Количество одновременно выгружаемых фалов не модет быть > "
                    + fileStorageProperties.getNumberOfUploadedFiles());
        }
    }
}
