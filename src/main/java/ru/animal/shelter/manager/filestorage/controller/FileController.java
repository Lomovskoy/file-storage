package ru.animal.shelter.manager.filestorage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.dto.FileMetaInfDTO;
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
    public FileMetaInfDTO getFile(@ApiParam("Идентификатор файла") @PathVariable UUID fileId) {
        var file = fileService.getFile(fileId);
        return fileMapper.fileToFileDtoMapper(file);
    }

    @ApiOperation("Загрузить файл")
    @PostMapping(value = "{userId}")
    @ResponseBody
    public FileMetaInfDTO saveFile(@RequestBody MultipartFile multipartFile, @PathVariable UUID userId,
                                                   @RequestParam(defaultValue = "") String description) {
        var fileMetaInf = fileService.saveFile(multipartFile, userId, description);
        return fileMapper.fileToFileDtoMapper(fileMetaInf);
    }

//    @PutMapping
//    @ApiOperation("Изменить файл")
//    public FileMetaInfDTO editFile(@ApiParam("Обьект описания файла") @RequestBody @Validated FileMetaInfDTO fileDTO) {
//        var file = fileMapper.fileDtoToFileMapper(fileDTO);
//        file = fileService.editFile(file);
//        return fileMapper.fileToFileDtoMapper(file);
//    }

    @DeleteMapping("{fileId}")
    @ApiOperation("Удалить файл")
    public void deleteFile(@ApiParam("Идентификатор файла") @PathVariable UUID fileId) {
        fileService.deleteFile(fileId);
    }
}
