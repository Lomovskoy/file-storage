package ru.animal.shelter.manager.filestorage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.aop.FileSizeValidation;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.model.dto.FileMetaInfDTO;
import ru.animal.shelter.manager.filestorage.service.FileService;
import ru.animal.shelter.manager.filestorage.service.impl.FileMetaInfServiceImpl;
import ru.animal.shelter.manager.filestorage.service.mappers.impl.FileMapperImpl;
import ru.animal.shelter.manager.filestorage.utils.MediaTypeUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Api("Контроллер для работы с файлами")
@RequestMapping(path = "v1/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    private final FileService fileService;
    private final FileMapperImpl fileMapper;
    private final FileMetaInfServiceImpl fileMetaInfService;

    @GetMapping(path = "{fileId}")
    @ApiOperation("Полйчить файл")
    public void getFile(@ApiParam("Идентификатор файла") @PathVariable UUID fileId,
                                                     HttpServletResponse response) throws IOException {
        fileService.getFile(fileId, response);
    }

    @GetMapping(path = "metaInf/{fileId}")
    @ApiOperation("Полйчить метаинформацию о файле")
    public FileMetaInf getMetaInfFile(@ApiParam("Идентификатор файла") @PathVariable UUID fileId){
        return fileMetaInfService.getMetaInfFile(fileId);
    }

    @FileSizeValidation
    @ApiOperation("Загрузить файл")
    @PostMapping(value = "{userId}")
    public FileMetaInfDTO saveFile(@RequestBody MultipartFile multipartFile, @PathVariable UUID userId,
                                   @RequestParam(defaultValue = "") String description) throws IOException {
        var fileMetaInf = fileService.saveFile(multipartFile, userId, description);
        return fileMapper.fileToFileDtoMapper(fileMetaInf);
    }

    @PutMapping
    @ApiOperation("Изменить файл")
    public FileMetaInfDTO editFile(@ApiParam("Обьект описания файла") @RequestBody @Validated FileMetaInfDTO fileDTO) {
        return null;
    }

    @DeleteMapping("{fileId}")
    @ApiOperation("Удалить файл")
    public void deleteFile(@ApiParam("Идентификатор файла") @PathVariable UUID fileId) throws IOException {
        fileService.deleteFile(fileId);
    }
}
