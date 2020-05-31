package ru.animal.shelter.manager.filestorage.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ApiModel("Модель для поиска по файлам")
public class SearchRequestFiles {

    @ApiParam("Имена файлов")
    List<String> fileName;

    @ApiParam("Расширения файлов")
    List<String> fileExt;

    @ApiParam("Описание файла")
    String description;

    @ApiParam("От какой даты создания файла искать")
    LocalDateTime createDateFrom;

    @ApiParam("До какой даты создания файла искать")
    LocalDateTime createDateBefore;

    @ApiParam("От какой даты изменения файла искать")
    LocalDateTime editDateFrom;

    @ApiParam("До какой даты изменения файла искать")
    LocalDateTime editDateBefore;

    @ApiParam("Текущая страница")
    Integer page;

    @ApiParam("Количество элементов на странице")
    Integer elements;
}
