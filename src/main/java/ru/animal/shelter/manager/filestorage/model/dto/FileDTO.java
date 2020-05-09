package ru.animal.shelter.manager.filestorage.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@ApiModel("Модель метаинформации о файле")
public class FileDTO {

    @ApiModelProperty("Идентификатор файла")
    UUID id;

    @ApiModelProperty("Имя файла")
    @Size(max = 255, message="Длина имени файла должна быть меньше 255")
    String fileName;

    @ApiModelProperty("Расширение файла")
    @Size(max = 10, message="Длина расширения файла должна быть меньше 10")
    String fileExt;

    @ApiModelProperty("Путь к файлу")
    String filePath;

    @ApiModelProperty("Дата создания файла")
    LocalDateTime createDate;

    @ApiModelProperty("Дата последнего изменения файла")
    LocalDateTime editDate;

    @ApiModelProperty("Идентификатор пользователя загрузившего файл")
    UUID userId;

    @ApiModelProperty("Размер файла")
    long size;
}
