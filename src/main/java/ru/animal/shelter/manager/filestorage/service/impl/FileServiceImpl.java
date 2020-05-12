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
import ru.animal.shelter.manager.filestorage.model.dto.RequestForMultipleFileDTO;
import ru.animal.shelter.manager.filestorage.service.FileMetaInfService;
import ru.animal.shelter.manager.filestorage.service.FileService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
    private static final String ZIP = ".zip";

    private final FileMetaInfService fileMetaInfService;
    private final FileStorageProperties fileStorageProperties;

    @Override
    public void getFile(UUID userId, UUID fileId, HttpServletResponse response) throws IOException {
        var fileMetaInf = fileMetaInfService.getMetaInfFile(userId, fileId);
        var file = getFile(fileMetaInf, getPath(fileMetaInf));
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
        var pathZipFile = getPath(fileMetaInfList.get(0));
        try(var zipOS = new ZipOutputStream(new FileOutputStream(pathZipFile + fileMetaInfList.get(0).getId() + ZIP))) {
            fileMetaInfList.forEach(fileMetaInf-> {
                var filename = getFile(fileMetaInf, getPath(fileMetaInf));
                try(var fileIn = new FileInputStream(filename)) {
                    var entry = new ZipEntry(fileMetaInf.getFileName() + "." + fileMetaInf.getFileExt());
                    zipOS.putNextEntry(entry);
                    byte[] buffer = new byte[fileIn.available()];
                    fileIn.read(buffer);
                    zipOS.write(buffer);
                } catch(IOException ex) {
                    LOG.error("Error uploading files by user: " + ex);
                    //throw new IOException("Error uploading file by user: " + ex);
                }
            });
            zipOS.closeEntry();
        } catch(IOException ex) {
            throw new IOException("Error uploading files by user: " + ex);
        }
    }

    @Override
    public FileMetaInf saveFile(MultipartFile multipartFile, UUID userId, String description) throws IOException {
        var fileMetaInf = fileMetaInfService.saveMetaInfFile(multipartFile, userId, description);
        var file = getFile(fileMetaInf, getPath(fileMetaInf));

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
        var file = getFile(fileMetaInf, getPath(fileMetaInf));

        if (file.delete()) {
            LOG.info("File: '" + file.getAbsolutePath() + "' deleted");
        } else {
            throw new IOException("Error deleting file: '" + file.getAbsolutePath());
        }

        fileMetaInfService.deleteMetaInfFile(fileId);
        LOG.info("Meta information of file: '" + file.getAbsolutePath() + "' deleted");
    }

    private String getPath(FileMetaInf fileBD) {
        var path = fileStorageProperties.getPath() + fileBD.getFilePath() + File.separator;
        checkDirs(path);
        return path;
    }

    private void checkDirs(String path) {
        var file = new File(path);

        if (!file.exists()) {
            if (file.mkdirs()) {
                LOG.info("Create new catalog: " + file.getPath());
            }
        }
    }

    private File getFile(FileMetaInf fileBD, String path) {
        var file = new File(path + fileBD.getId());
        checkFile(file);
        return file;
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

    private void checkFileList(RequestForMultipleFileDTO request, List<FileMetaInf> fileMetaInfList) {
        if (fileMetaInfList.isEmpty())
            throw new ValidationException("The list of files turned out to be empty for the user: " + request.getUserId());
    }
}
