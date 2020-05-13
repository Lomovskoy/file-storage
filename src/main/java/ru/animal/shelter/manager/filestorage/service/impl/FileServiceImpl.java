package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.model.dto.RequestForMultipleFileDTO;
import ru.animal.shelter.manager.filestorage.service.FileMetaInfService;
import ru.animal.shelter.manager.filestorage.service.FileService;
import ru.animal.shelter.manager.filestorage.utils.FileUtils;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
    private static final String UTF_8 = "UTF-8";

    private final FileUtils fileUtils;
    private final ArchiveServiceImpl archiveService;
    private final FileMetaInfService fileMetaInfService;

    @Override
    public void getFile(UUID userId, UUID fileId, HttpServletResponse response) throws IOException {
        var fileMetaInf = fileMetaInfService.getMetaInfFile(userId, fileId);
        var file = fileUtils.getFile(fileMetaInf, fileUtils.getPath(fileMetaInf));
        setResponse(response, fileMetaInf);
        try (var fileInputStream = new FileInputStream(file)){
            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
            LOG.info("File uploaded successfully by user");
        } catch (IOException ex){
            throw new IOException("Error uploading file by user: " + ex);
        }
    }

    @Override
    public void getManyFile(RequestForMultipleFileDTO request, HttpServletResponse response) throws IOException  {
        var fileMetaInfList = fileMetaInfService.getManyMetaInfFile(request);
        checkFileList(request, fileMetaInfList);
        var archivePath = archiveService.saveFilesToArchive(fileMetaInfList);
        archiveService.getArchive(response, archivePath);
        archiveService.deleteArchive(archivePath);
    }

    @Override
    public FileMetaInf saveFile(MultipartFile multipartFile, UUID userId, String description) throws IOException {
        var fileMetaInf = fileMetaInfService.saveMetaInfFile(multipartFile, userId, description);
        var file = fileUtils.getFile(fileMetaInf, fileUtils.getPath(fileMetaInf));

        try (var fileOutputStream = new FileOutputStream(file)){
            FileCopyUtils.copy(multipartFile.getBytes(), fileOutputStream);
            LOG.info("File successfully written to disk");
        } catch (IOException ex){
            fileMetaInfService.deleteMetaInfFile(fileMetaInf.getId());
            throw new IOException("Error writing file to disk: " + ex);
        }
        return fileMetaInf;
    }

    @Override
    public void deleteFile(UUID userId, UUID fileId) throws IOException {
        var fileMetaInf = fileMetaInfService.getMetaInfFile(userId, fileId);
        var file = fileUtils.getFile(fileMetaInf, fileUtils.getPath(fileMetaInf));

        if (file.delete()) {
            LOG.info("File: '" + file.getAbsolutePath() + "' deleted");
        } else {
            throw new IOException("Error deleting file: '" + file.getAbsolutePath());
        }

        fileMetaInfService.deleteMetaInfFile(fileId);
        LOG.info("Meta information of file: '" + file.getAbsolutePath() + "' deleted");
    }

    private void setResponse(HttpServletResponse response, FileMetaInf fileMetaInf) {
        var fileName = URLEncoder.encode(fileMetaInf.getFileName(), StandardCharsets.UTF_8);
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
                        fileName + "." + fileMetaInf.getFileExt());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLengthLong(fileMetaInf.getSize());
        response.setCharacterEncoding(UTF_8);
    }

    private void checkFileList(RequestForMultipleFileDTO request, List<FileMetaInf> fileMetaInfList) {
        if (fileMetaInfList.isEmpty())
            throw new ValidationException("The list of files turned out to be empty for the user: " + request.getUserId());
    }
}
