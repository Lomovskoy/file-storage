package ru.animal.shelter.manager.filestorage.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@ApiModel("Модель метаинформации о файле")
public class FileDTO {

    @ApiModelProperty("Идентификатор файла")
    UUID id;

    @ApiModelProperty("Имя файла")
    String fileName;

    @ApiModelProperty("Расширение файла")
    String fileExt;

    @ApiModelProperty("Путь к файлу")
    String filePath;

    @ApiModelProperty("Дата создания файла")
    LocalDateTime createDate;

    @ApiModelProperty("Дата последнего изменения файла")
    LocalDateTime editDate;

    @ApiModelProperty("Идентификатор пользователя загрузившего файл")
    UUID userId;

    @ApiModelProperty("Описание файла")
    String description;

    @ApiModelProperty("Размер файла")
    long size;
}
