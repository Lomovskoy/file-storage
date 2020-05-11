package ru.animal.shelter.manager.filestorage.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaInf {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    @Size(max = 255, min = 1, message = "Имя файла должно быть от 1 до 255 символов")
    private String fileName;

    @NonNull
    @Size(max = 10, min = 1, message = "Расширение файла должно быть от 1 до 10 символов")
    private String fileExt;

    @NonNull
    @Size(max = 10, min = 10, message = "Путь файла должно быть 10 символов")
    private String filePath;

    @NonNull
    private LocalDateTime createDate;

    @NonNull
    private LocalDateTime editDate;

    @NonNull
    private UUID userId;

    @Size(max = 500, message = "Описание файла не должно превышать 500 символов")
    private String description;

    long size;
}