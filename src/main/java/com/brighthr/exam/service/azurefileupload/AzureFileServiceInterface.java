package com.brighthr.exam.service.azurefileupload;

import org.springframework.web.multipart.MultipartFile;

public interface AzureFileServiceInterface {
    public String uploadFileUpload(String containerName, String path);

    public Long checkFileSize(String path);
}
