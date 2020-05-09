package ru.animal.shelter.manager.filestorage.service;

import ru.animal.shelter.manager.filestorage.model.File;
import java.util.UUID;

public interface FileService {

    File getFile(UUID fileId);

    File saveFile(File file);

    File editFile(File file);

    void deleteFile(UUID fileId);
}
