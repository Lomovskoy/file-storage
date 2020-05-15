package ru.animal.shelter.manager.filestorage.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.dto.RequestForMultipleFileDTO;

import java.util.Arrays;

public class Validation {

    protected String[] getNames(JoinPoint point) {
        var signature = (CodeSignature)point.getSignature();
        return signature.getParameterNames();
    }

    protected void checkName(String[] names, String name) {
        if (!(names != null && names.length > 0))
            throw new IllegalArgumentException(String.format("Not found method argument with name '%s'", name));
    }

    protected MultipartFile getValueMultipart(JoinPoint point, String[] names, String name) {
        var formatArgIndex = Arrays.asList(names).indexOf(name);
        checkArgIndex(formatArgIndex, name);
        return (MultipartFile)point.getArgs()[formatArgIndex];
    }

    protected RequestForMultipleFileDTO getValueFileNumber(JoinPoint point, String[] names, String name) {
        var formatArgIndex = Arrays.asList(names).indexOf(name);
        checkArgIndex(formatArgIndex, name);
        return (RequestForMultipleFileDTO)point.getArgs()[formatArgIndex];
    }

    protected void checkArgIndex(int userArgIndex, String name) {
        if (userArgIndex == -1){
            throw new IllegalArgumentException(String.format("Not found method argument with name '%s'", name));
        }
    }
}
