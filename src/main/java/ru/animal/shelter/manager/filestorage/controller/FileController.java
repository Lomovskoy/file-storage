package ru.animal.shelter.manager.filestorage.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.animal.shelter.manager.filestorage.model.File;
import ru.animal.shelter.manager.filestorage.model.dto.FileDTO;
import ru.animal.shelter.manager.filestorage.service.FileService;
import ru.animal.shelter.manager.filestorage.service.mappers.impl.FileMapperImpl;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {

    FileService fileService;
    FileMapperImpl fileMapper;

    @GetMapping("{fileId}")
    public FileDTO getFile(@PathVariable UUID fileId) {
        var file = fileService.getFile(fileId);
        return fileMapper.fileToFileDtoMapper(file);
    }

    @PostMapping
    public FileDTO saveFile(@RequestBody @Validated FileDTO fileDTO) {
        var file = fileMapper.fileDtoToFileMapper(fileDTO);
        file = fileService.saveFile(file);
        return fileMapper.fileToFileDtoMapper(file);
    }

    @PutMapping
    public FileDTO editFile(@RequestBody @Validated FileDTO fileDTO) {
        var file = fileMapper.fileDtoToFileMapper(fileDTO);
        file = fileService.editFile(file);
        return fileMapper.fileToFileDtoMapper(file);
    }

    @DeleteMapping("{fileId}")
    public void deleteFile(@PathVariable UUID fileId) {
        fileService.deleteFile(fileId);
    }
}
