package ru.animal.shelter.manager.filestorage.service.impl;

import ru.animal.shelter.manager.filestorage.model.FileMetaInf;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BaseService {

    protected String getFileName(FileMetaInf fileMetaInf) {
        return URLEncoder.encode(fileMetaInf.getFileName(), StandardCharsets.UTF_8);
    }

}
