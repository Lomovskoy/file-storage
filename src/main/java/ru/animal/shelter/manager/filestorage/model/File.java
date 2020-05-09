package ru.animal.shelter.manager.filestorage.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    
    String fileName;
    
    String fileExt;
    
    String filePath;
    
    LocalDateTime createDate;

    LocalDateTime editDate;

    UUID userId;
    
    long size;
}