package ru.animal.shelter.manager.filestorage.utils;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;

@Service
public class MediaTypeUtils {

    public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        String mineType = servletContext.getMimeType(fileName);
        try {
            return MediaType.parseMediaType(mineType);
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
