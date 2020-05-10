package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.repository.FileRepository;
import ru.animal.shelter.manager.filestorage.service.FileService;
import ru.animal.shelter.manager.filestorage.service.mappers.impl.FileMapperImpl;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    FileMapperImpl fileMapper;
    FileRepository fileRepository;

    @Override
    public FileMetaInf getFile(UUID fileId) {
        return fileRepository.findById(fileId).orElse(new FileMetaInf());
    }

    @Override
    public FileMetaInf saveFile(MultipartFile file, UUID userId, String description) {
        var fileMetaInf = fileMapper.requestSaveFileToFileMetaInfMapper(file, userId, description);
        return fileRepository.save(fileMetaInf);
    }

    @Override
    public FileMetaInf editFile(FileMetaInf file) {
        return fileRepository.save(file);
    }

    @Override
    public void deleteFile(UUID fileId) {
        fileRepository.deleteById(fileId);
    }
}
