package ru.animal.shelter.manager.filestorage.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Validated
@AllArgsConstructor
@ApiModel("Модель для поиска по файлам")
public class SearchRequestFiles {

    @ApiParam(value = "Идентификатор пользователя")
    @NotNull
    UUID userId;

    @ApiParam(value = "Имя файла")
    String fileName;

    @ApiParam(value = "Расширения файлов")
    List<String> fileExt;

    @ApiParam(value = "Описание файла")
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
    @NotNull
    Integer pageNumber;

    @ApiParam(value = "Количество элементов на странице", example = "10")
    @Min(1)
    @NotNull
    Integer pageSize;

    @NotNull
    @ApiParam(value = "Метод сортировки", example = "ASC")
    Sort.Direction sort;

    @NotNull
    @ApiParam(value = "Атрибут сортировки")
    FileSortProperty sortProperty;
}
