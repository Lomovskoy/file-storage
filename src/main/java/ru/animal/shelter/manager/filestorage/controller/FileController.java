package ru.animal.shelter.manager.filestorage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.aop.FileSizeValidation;
import ru.animal.shelter.manager.filestorage.model.dto.FileMetaInfDTO;
import ru.animal.shelter.manager.filestorage.service.FileService;
import ru.animal.shelter.manager.filestorage.service.impl.FileMetaInfServiceImpl;
import ru.animal.shelter.manager.filestorage.service.mappers.impl.FileMapperImpl;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Api("Контроллер для работы с файлами")
@RequestMapping(path = "v1/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    private final FileService fileService;
    private final FileMapperImpl fileMapper;
    private final FileMetaInfServiceImpl fileMetaInfService;

    @GetMapping("/{userId}/{fileId}")
    @ApiOperation("Полйчить файл")
    public void getFile(@ApiParam("Идентификатор файла") @PathVariable UUID fileId,
                        @ApiParam("Идентификатор пользователя") @PathVariable UUID userId,
                        HttpServletResponse response) throws IOException {
        fileService.getFile(userId, fileId, response);
    }

    @GetMapping("metaInf/{userId}/{fileId}")
    @ApiOperation("Полйчить метаинформацию о файле")
    public FileMetaInfDTO getMetaInfFile(@ApiParam("Идентификатор файла") @PathVariable UUID fileId,
                                         @ApiParam("Идентификатор пользователя") @PathVariable UUID userId){
        var fileMetaInf = fileMetaInfService.getMetaInfFile(userId, fileId);
        return fileMapper.fileToFileDtoMapper(fileMetaInf);
    }

    @FileSizeValidation
    @ApiOperation("Загрузить файл")
    @PostMapping("{userId}")
    public FileMetaInfDTO saveFile(@ApiParam("Файл") @RequestBody MultipartFile multipartFile,
                                   @ApiParam("Идентификатор пользователя") @PathVariable UUID userId,
                                   @ApiParam("Описани файла") @RequestParam(defaultValue = "") String description) throws IOException {
        var fileMetaInf = fileService.saveFile(multipartFile, userId, description);
        return fileMapper.fileToFileDtoMapper(fileMetaInf);
    }

    @PutMapping("{userId}/{fileId}")
    @ApiOperation("Изменить метаинформацию о файле")
    public FileMetaInfDTO editFile(@ApiParam("Идентификатор пользователя") @PathVariable UUID userId,
                                   @ApiParam("Идентификатор файла") @PathVariable UUID fileId,
                                   @ApiParam("Описани файла") @RequestParam(defaultValue = "") String description) {
        var fileMetaInf = fileMetaInfService.editMetaInfFile(userId, fileId, description);
        return fileMapper.fileToFileDtoMapper(fileMetaInf);
    }

    @DeleteMapping("{userId}/{fileId}")
    @ApiOperation("Удалить файл")
    public void deleteFile(@ApiParam("Идентификатор пользователя") @PathVariable UUID userId,
                           @ApiParam("Идентификатор файла") @PathVariable UUID fileId) throws IOException {
        fileService.deleteFile(userId, fileId);
    }
}
