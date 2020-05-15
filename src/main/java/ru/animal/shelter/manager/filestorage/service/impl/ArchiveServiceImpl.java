package ru.animal.shelter.manager.filestorage.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import ru.animal.shelter.manager.filestorage.model.FileMetaInf;
import ru.animal.shelter.manager.filestorage.service.ArchiveService;
import ru.animal.shelter.manager.filestorage.utils.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class ArchiveServiceImpl implements ArchiveService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
    private static final String ZIP = "zip";
    private static final int BYTE = 1024;
    private static final String NAME = "archive_files_for_";
    private static final int COMPRESSION = 5;

    private final Clock clock;
    private final FileUtils fileUtils;

    @Override
    public String saveFilesToArchive(List<FileMetaInf> fileMetaInfList) throws IOException {
        var pathZipFile = fileUtils.getPath(fileMetaInfList.get(0));
        var nameZipFile = getNameZipFile(pathZipFile);

        try(var zipOS = new ZipOutputStream(new FileOutputStream(nameZipFile), StandardCharsets.UTF_8)) {
            zipOS.setLevel(COMPRESSION);
            for (var fileMetaInf: fileMetaInfList) {
                var filename = fileUtils.getFile(fileMetaInf, fileUtils.getPath(fileMetaInf));
                    var ze = new ZipEntry(fileMetaInf.getFileName() + "." + fileMetaInf.getFileExt());
                    zipOS.putNextEntry(ze);
                    try (var fileIn = new FileInputStream(filename)) {
                        write(fileIn, zipOS);
                    }
                    zipOS.closeEntry();
            }
        } catch(IOException ex) {
            throw new IOException("Error created archive: " + ex);
        }
        var fileIds = fileMetaInfList.stream().map(f-> f.getId().toString()).collect(Collectors.toList());
        LOG.info(String.format("File archive created: %s",  fileIds));
        return nameZipFile;
    }

    @Override
    public void getArchive(HttpServletResponse response, String path) throws IOException {
        setResponse(response, getNameArchive());
        try (var zipInputStream = new ZipInputStream(new FileInputStream(path))){
            FileCopyUtils.copy(zipInputStream, response.getOutputStream());
            LOG.info("Archive uploaded successfully by user");
        } catch (IOException ex){
            throw new IOException("Error uploading archive by user: " + ex);
        }
    }

    @Override
    public void deleteArchive(String path) throws IOException {
        var file = new File(path);

        if (file.delete()) {
            LOG.info("Archive: '" + file.getAbsolutePath() + "' deleted");
        } else {
            throw new IOException("Error deleting archive: '" + file.getAbsolutePath());
        }
    }

    private FileMetaInf getNameArchive() {
        var fileMetaInf = new FileMetaInf();
        fileMetaInf.setFileName(NAME + LocalDate.now(clock));
        fileMetaInf.setFileExt(ZIP);
        return fileMetaInf;
    }

    private String getNameZipFile(String pathZipFile) {
        return pathZipFile + UUID.randomUUID() + "." + ZIP;
    }

    private void setResponse(HttpServletResponse response, FileMetaInf fileMetaInf) {
        var fileName = URLEncoder.encode(fileMetaInf.getFileName(), StandardCharsets.UTF_8);
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" +
                        fileName + "." + fileMetaInf.getFileExt());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLengthLong(fileMetaInf.getSize());
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
    }

    void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BYTE];
        while (in.read(buffer) != -1) {
            out.write(buffer);
        }
    }
}
