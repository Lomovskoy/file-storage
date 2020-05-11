package ru.animal.shelter.manager.filestorage.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ApiModel("Модель для получения онформации о множестве файлов")
public class RequestForMultipleFileDTO {

    @NonNull
    @Size(min = 1)
    @ApiParam("Идентификатор файла")
    List<UUID> fileId;

    @NonNull
    @ApiParam("Идентификатор пользователя")
    UUID userId;

}
