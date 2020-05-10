package ru.animal.shelter.manager.filestorage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.animal.shelter.manager.filestorage.model.dto.FileDTO;
import ru.animal.shelter.manager.filestorage.service.FileService;
import ru.animal.shelter.manager.filestorage.service.mappers.impl.FileMapperImpl;

import java.util.UUID;

@RestController
@AllArgsConstructor
@Api("Контроллер для работы с файлами")
@RequestMapping(path = "v1/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    FileService fileService;
    FileMapperImpl fileMapper;

    @GetMapping("{fileId}")
    @ApiOperation("Полйчить файл")
    public FileDTO getFile(@ApiParam("Идентификатор файла") @PathVariable UUID fileId) {
        var file = fileService.getFile(fileId);
        return fileMapper.fileToFileDtoMapper(file);
    }

    @PostMapping
    @ApiOperation("Загрузить файл")
    public FileDTO saveFile(@ApiParam("Обьект описания файла") @RequestBody @Validated FileDTO fileDTO) {
        var file = fileMapper.fileDtoToFileMapper(fileDTO);
        file = fileService.saveFile(file);
        return fileMapper.fileToFileDtoMapper(file);
    }

    @PutMapping
    @ApiOperation("Изменить файл")
    public FileDTO editFile(@ApiParam("Обьект описания файла") @RequestBody @Validated FileDTO fileDTO) {
        var file = fileMapper.fileDtoToFileMapper(fileDTO);
        file = fileService.editFile(file);
        return fileMapper.fileToFileDtoMapper(file);
    }

    @DeleteMapping("{fileId}")
    @ApiOperation("Удалить файл")
    public void deleteFile(@ApiParam("Идентификатор файла") @PathVariable UUID fileId) {
        fileService.deleteFile(fileId);
    }
}
