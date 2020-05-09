package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.animal.shelter.manager.filestorage.model.File;
import ru.animal.shelter.manager.filestorage.repository.FileRepository;
import ru.animal.shelter.manager.filestorage.service.FileService;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    FileRepository fileRepository;

    @Override
    public File getFile(UUID fileId) {
        return fileRepository.findById(fileId).orElse(new File());
    }

    @Override
    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    @Override
    public File editFile(File file) {
        return fileRepository.save(file);
    }

    @Override
    public void deleteFile(UUID fileId) {
        fileRepository.deleteById(fileId);
    }
}
