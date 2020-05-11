package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.animal.shelter.manager.filestorage.config.FileStorageProperties;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.service.FileMetaInfService;
import ru.animal.shelter.manager.filestorage.service.FileService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    FileMetaInfService fileMetaInfService;
    FileStorageProperties fileStorageProperties;

    @Override
    public void getFile(UUID fileId, HttpServletResponse response) throws IOException {
        var fileMetaInf = fileMetaInfService.getMetaInfFile(fileId);
        var file = new File(getPath(fileMetaInf) + File.separator + fileMetaInf.getId().toString());
        setResponse(response, fileMetaInf);
        checkFile(file);
        try (var fileInputStream = new FileInputStream(file)){
            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
            LOG.info("File uploaded successfully by user");
        } catch (IOException ex){
            throw new IOException("Error uploading file by user: " + ex);
        }
    }

    @Override
    public FileMetaInf saveFile(MultipartFile multipartFile, UUID userId, String description) throws IOException {
        var fileMetaInf = fileMetaInfService.saveMetaInfFile(multipartFile, userId, description);
        var path = getPath(fileMetaInf);
        checkDirs(path);

        try (var fileOutputStream = new FileOutputStream(getFile(fileMetaInf, path))){
            FileCopyUtils.copy(multipartFile.getBytes(), fileOutputStream);
            LOG.info("File successfully written to disk");
        } catch (IOException ex){
            fileMetaInfService.deleteMetaInfFile(fileMetaInf.getId());
            throw new IOException("Error writing file to disk: " + ex);
        }
        return fileMetaInf;
    }

    @Override
    public void deleteFile(UUID fileId) throws IOException {
        var fileMetaInf = fileMetaInfService.getMetaInfFile(fileId);
        var file = new File(getPath(fileMetaInf) + File.separator + fileMetaInf.getId().toString());
        checkFile(file);

        if (file.delete()) {
            LOG.info("File: '" + file.getAbsolutePath() + "' deleted");
        } else {
            throw new IOException("Error deleting file: '" + file.getAbsolutePath());
        }

        fileMetaInfService.deleteMetaInfFile(fileId);
        LOG.info("Meta information of file: '" + file.getAbsolutePath() + "' deleted");
    }

    private String getPath(FileMetaInf fileBD) {
        return fileStorageProperties.getPath() + File.separator + fileBD.getFilePath();
    }

    private void checkDirs(String path) {
        var file = new File(path);

        if (!file.exists())
            if (file.mkdirs())
                LOG.info("Create new catalog: " + file.getPath());
    }

    private File getFile(FileMetaInf fileBD, String path) {
        return new File(path + File.separator + fileBD.getId());
    }

    private void checkFile(File file) {
        if (!file.exists()) {
            var errMessage = String.format("Файл %s не существует", file.getAbsolutePath());
            throw new ValidationException(errMessage);
        }
    }

    private void setResponse(HttpServletResponse response, FileMetaInf fileMetaInf) {
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
                        fileMetaInf.getFileName() + "." +
                        fileMetaInf.getFileExt());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLengthLong(fileMetaInf.getSize());
    }
}
