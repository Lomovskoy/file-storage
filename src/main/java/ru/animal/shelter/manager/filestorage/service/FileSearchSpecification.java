package ru.animal.shelter.manager.filestorage.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.model.dto.SearchRequestFiles;

public interface FileSearchSpecification {

    Specification<FileMetaInf> getSpecificationForFilters(SearchRequestFiles searchRequestFiles);

    Pageable getPage(SearchRequestFiles requestPage);
}
