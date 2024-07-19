package com.brighthr.exam.web.rest;

import com.brighthr.exam.domain.File;
import com.brighthr.exam.repository.FileRepository;
import com.brighthr.exam.service.azurefileupload.AzureFileService;
import java.io.InputStream;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class FileController {

    private final String AZURE_SV =
        "sv=2022-11-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2024-12-18T18:42:13Z&st=2024-07-18T09:42:13Z&spr=https,http&sig=a3mzU00%2BJvq2PQKmiNgKGGqoxdC%2FaXCrefwIm7Dnz5o%3D";

    @Autowired
    private AzureFileService azureFileUploadService;

    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/download/file/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) {
        Optional<File> fileOptional = fileRepository.findById(id);

        if (fileOptional.isPresent()) {
            File file = fileOptional.get();

            // Remove spaces from the file name
            String sanitizedFileName = file.getName().replace(" ", "");

            // Append the file type at the end of the sanitized file name
            String fileType = file.getFiletype().getTypeName().toLowerCase();
            String finalFileName = sanitizedFileName + "." + fileType;

            if (file.getFileUrl() != null) {
                try {
                    String downloadLink = file.getFileUrl() + "?" + AZURE_SV;
                    InputStream blobStream = azureFileUploadService.downloadBlob(downloadLink);
                    HttpHeaders headers = new HttpHeaders();

                    headers.setContentDispositionFormData("inline", finalFileName);

                    // Determine content type based on file extension

                    MediaType mediaType;
                    switch (fileType) {
                        case "png":
                            mediaType = MediaType.IMAGE_PNG;
                            break;
                        case "jpg":
                        case "jpeg":
                            mediaType = MediaType.IMAGE_JPEG;
                            break;
                        case "pdf":
                            mediaType = MediaType.APPLICATION_PDF;
                            break;
                        case "doc":
                            mediaType = MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                            break;
                        case "docx":
                            mediaType = MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                            break;
                        case "csv":
                            mediaType = MediaType.TEXT_PLAIN;
                            break;
                        case "mp4":
                            mediaType = MediaType.valueOf("video/mp4");
                            break;
                        default:
                            mediaType = MediaType.APPLICATION_OCTET_STREAM;
                            break;
                    }
                    headers.setContentType(mediaType);

                    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(blobStream));
                } catch (Exception e) {
                    throw new RuntimeException("Error downloading  file", e);
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
