package ru.animal.shelter.manager.filestorage.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ApiModel("Модель метаинформации о файле")
public class FileMetaInfDTO {

    @ApiModelProperty("Идентификатор файла")
    private final UUID id;

    @NonNull
    @ApiModelProperty("Имя файла")
    private final String fileName;

    @NonNull
    @ApiModelProperty("Расширение файла")
    private final String fileExt;

    @NonNull
    @ApiModelProperty("Дата создания файла")
    private final LocalDateTime createDate;

    @NonNull
    @ApiModelProperty("Дата последнего изменения файла")
    private final LocalDateTime editDate;

    @NonNull
    @ApiModelProperty("Идентификатор пользователя загрузившего файл")
    private final UUID userId;

    @NonNull
    @ApiModelProperty("Описание файла")
    private final String description;

    @ApiModelProperty("Размер файла")
    private final long size;
}
