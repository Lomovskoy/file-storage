package ru.animal.shelter.manager.filestorage.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ApiModel("Модель для поиска по файлам")
public class SearchRequestFiles {

    @ApiParam(value = "Идентификатор пользователя")
    @NotNull
    UUID userId;

    @ApiParam(value = "Имя файла", example = " ")
    String fileName;

    @ApiParam(value = "Расширения файлов", example = " ")
    List<String> fileExt;

    @ApiParam(value = "Описание файла", example = " ")
    String description;

    @ApiParam("От какой даты создания файла искать")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate createDateFrom;

    @ApiParam("До какой даты создания файла искать")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate createDateBefore;

    @ApiParam("От какой даты изменения файла искать")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate editDateFrom;

    @ApiParam("До какой даты изменения файла искать")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate editDateBefore;

    @ApiParam(value = "Текущая страница", example = "0")
    @Min(0)
    Integer pageNumber;

    @ApiParam(value = "Количество элементов на странице", example = "1")
    @Min(1)
    Integer pageSize;

    @ApiParam(value = "Метод сортировки", example = "ASC")
    Sort.Direction sort;

}
