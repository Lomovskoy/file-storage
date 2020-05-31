package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.model.dto.FileSortProperty;
import ru.animal.shelter.manager.filestorage.model.dto.SearchRequestFiles;
import ru.animal.shelter.manager.filestorage.service.FileSearchSpecification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileSearchSpecificationImpl implements FileSearchSpecification {

    private static final Logger LOG = LoggerFactory.getLogger(FileSearchSpecificationImpl.class);
    private static final Locale rusLocale = new Locale("ru", "RU");

    @Override
    public Specification<FileMetaInf> getSpecificationForFilters(SearchRequestFiles searchRequestFiles) {
        LOG.info("Specification forming: " + searchRequestFiles);
        return (root, query, criteriaBuilder) ->
                Specification
                        .where(equalsUserId(searchRequestFiles.getUserId()))
                        .and(likeFileName(searchRequestFiles.getFileName()))
                        .and(equalsFileExt(searchRequestFiles.getFileExt()))
                        .and(likeDescription(searchRequestFiles.getDescription()))
                        .and(greaterThanOrEqualToCreateFile(searchRequestFiles.getCreateDateBefore()))
                        .and(lessThanOrEqualToCreateFile(searchRequestFiles.getCreateDateFrom()))
                        .and(greaterThanOrEqualToEditFile(searchRequestFiles.getEditDateBefore()))
                        .and(lessThanOrEqualToEditFile(searchRequestFiles.getEditDateFrom()))
                        .toPredicate(root, query.distinct(true), criteriaBuilder);
    }

    @Override
    public Pageable getPage(SearchRequestFiles requestPage) {
        var sortPropertyName = sortPropertyName(requestPage.getSortProperty());
        var sort = Sort.by(new Sort.Order(requestPage.getSort(), sortPropertyName));
        return PageRequest.of(requestPage.getPageNumber(), requestPage.getPageSize(), sort);
    }

    public String sortPropertyName(FileSortProperty sortProperty) {
        switch (sortProperty) {
            case FILE_NAME:
                return FileMetaInf.PROPERTY_NAME_FILE_NAME;
            case FILE_EXT:
                return FileMetaInf.PROPERTY_NAME_FILE_EXT;
            case DESCRIPTION:
                return FileMetaInf.PROPERTY_NAME_DESCRIPTION;
            case CREATE_DATE:
                return FileMetaInf.PROPERTY_NAME_CREATE_DATE;
            case EDIT_DATE:
                return FileMetaInf.PROPERTY_NAME_EDIT_DATE;
            default:
                throw new UnsupportedOperationException("Unhandled file sort property " + sortProperty);
        }
    }

    private Specification<FileMetaInf> equalsUserId(UUID userId) {
        if (userId == null)
            return null;
        return (fileMetaInfRoot, cq, cb) -> cb.equal(fileMetaInfRoot.get(FileMetaInf.PROPERTY_NAME_USER_ID), userId);
    }

    private Specification<FileMetaInf> likeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty())
            return null;

        var fileNameTrim = fileName.trim();
        if (fileNameTrim.equals(""))
            return null;

        return (fileMetaInfRoot, cq, cb) ->
                buildFinalString(fileNameTrim, fileMetaInfRoot, cb, FileMetaInf.PROPERTY_NAME_FILE_NAME);
    }

    private Specification<FileMetaInf> likeDescription(String description) {
        if (description == null || description.isEmpty())
            return null;

        var descriptionTrim = description.trim();
        if (descriptionTrim.equals(""))
            return null;

        return (fileMetaInfRoot, cq, cb) ->
                buildFinalString(descriptionTrim, fileMetaInfRoot, cb, FileMetaInf.PROPERTY_NAME_DESCRIPTION);
    }

    private Specification<FileMetaInf> equalsFileExt(List<String> fileExt) {
        if (fileExt == null || fileExt.isEmpty())
            return null;
        var fileExtTrim = fileExt.stream().map(String::trim).filter(str->
                !str.equals("")).collect(Collectors.toList());

        return (fileMetaInfRoot, cq, cb) -> fileMetaInfRoot.get(FileMetaInf.PROPERTY_NAME_FILE_EXT).in(fileExtTrim);
    }

    private Specification<FileMetaInf> greaterThanOrEqualToCreateFile(LocalDate createDateBefore){
        if (createDateBefore == null)
            return null;
        var beforeLocalDateTime = getStartDate(createDateBefore);
        return (report, cq, cb) ->
                cb.greaterThanOrEqualTo(report.get(FileMetaInf.PROPERTY_NAME_CREATE_DATE), beforeLocalDateTime);
    }

    private Specification<FileMetaInf> lessThanOrEqualToCreateFile(LocalDate createDateFrom){
        if (createDateFrom == null)
            return null;
        var fromLocalDateTime = getStopDate(createDateFrom);
        return (report, cq, cb) ->
                cb.lessThanOrEqualTo(report.get(FileMetaInf.PROPERTY_NAME_CREATE_DATE), fromLocalDateTime);
    }

    private Specification<FileMetaInf> greaterThanOrEqualToEditFile(LocalDate editDateBefore){
        if (editDateBefore == null)
            return null;
        var beforeLocalDateTime = getStartDate(editDateBefore);
        return (report, cq, cb) ->
                cb.greaterThanOrEqualTo(report.get(FileMetaInf.PROPERTY_NAME_CREATE_DATE), beforeLocalDateTime);
    }

    private Specification<FileMetaInf> lessThanOrEqualToEditFile(LocalDate editDateFrom){
        if (editDateFrom == null)
            return null;
        var fromLocalDateTime = getStopDate(editDateFrom);
        return (report, cq, cb) ->
                cb.lessThanOrEqualTo(report.get(FileMetaInf.PROPERTY_NAME_CREATE_DATE), fromLocalDateTime);
    }

    private Predicate buildFinalString(String fileName, Root<FileMetaInf> fileMetaInfRoot,
                                       CriteriaBuilder cb, String fieldName) {
        return cb.like(cb.lower(fileMetaInfRoot.get(fieldName)), "%" +
                finalSubStringBuilder(fileName).toLowerCase(rusLocale) + "%");
    }

    private String finalSubStringBuilder(String subString) {
        return subString.trim().replace("\\", "\\\\").replace("%", "\\%");
    }

    private LocalDateTime getStartDate(LocalDate createDateBefore) {
        return createDateBefore == null ? LocalDateTime.MIN : LocalDateTime.of(createDateBefore, LocalTime.MIN);
    }

    private LocalDateTime getStopDate(LocalDate createDateFrom) {
        return createDateFrom == null ? LocalDateTime.MAX : LocalDateTime.of(createDateFrom, LocalTime.MAX);
    }
}
