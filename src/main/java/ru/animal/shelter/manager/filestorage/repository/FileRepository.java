package ru.animal.shelter.manager.filestorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileMetaInf, UUID>, JpaSpecificationExecutor<FileMetaInf> {

    List<FileMetaInf> findByIdInAndUserId(List<UUID> id, UUID userId);
}
