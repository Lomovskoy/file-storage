package ru.animal.shelter.manager.filestorage.service;

import ru.animal.shelter.manager.filestorage.model.FileMetaInf;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ArchiveService {

    String saveFilesToArchive(List<FileMetaInf> fileMetaInfList) throws IOException;

    void getArchive(HttpServletResponse response, String path) throws IOException;

    void deleteArchive(String path) throws IOException;
}
