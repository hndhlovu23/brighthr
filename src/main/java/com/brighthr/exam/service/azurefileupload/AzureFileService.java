package com.brighthr.exam.service.azurefileupload;

import com.azure.storage.blob.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AzureFileService implements AzureFileServiceInterface {

    protected final Log logger = LogFactory.getLog(getClass());

    // Your Azure Blob Storage account key
    private final String accountKey ="";

    // Your Azure Blob Storage account name
    private final String storageAccountName = "";

    // Azure Blob Storage connection string template
    private final String connectString =
        "DefaultEndpointsProtocol=https;AccountName=" +
        storageAccountName +
        ";AccountKey=" +
        accountKey +
        ";EndpointSuffix=core.windows.net";

    /**
     * Uploads a file to Azure Blob Storage.
     *
     * @param containerName The name of the container to upload the file to.
     * @param path          The path of the file to upload.
     * @return The URL of the uploaded blob.
     */
    @Override
    public String uploadFileUpload(String containerName, String path) {
        String azureFileLink = null;

        try {
            // Create a BlobServiceClient object to connect to the Azure Blob Storage
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectString).buildClient();

            // Get a BlobContainerClient object to access the container
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Get a BlobClient object to access the specific blob
            BlobClient blobClient = containerClient.getBlobClient(path);

            // Check if the blob already exists
            if (!blobClient.exists()) {
                // Upload the file to the blob
                InputStream fileStream = getResourceAsStream(path);
                if (fileStream != null) {
                    blobClient.upload(fileStream, fileStream.available(), true);
                    logger.info("File uploaded to Azure Blob Storage: " + path);

                    // Set the azureFileLink to the URL of the uploaded blob
                    azureFileLink = blobClient.getBlobUrl();
                } else {
                    logger.error("File not found: " + path);
                }
            } else {
                // Log that the file already exists in the blob storage
                logger.info("File already exists in Azure Blob Storage: " + path);

                // Set the azureFileLink to the URL of the existing blob
                azureFileLink = blobClient.getBlobUrl();
            }
        } catch (Exception e) {
            // Log any errors that occur during the upload process
            logger.error("Error uploading file to Azure Blob Storage: " + path, e);
        }

        return azureFileLink;
    }

    /**
     * Check file size*
     * @param path to file location
     * @ Long response with file type.
     */
    @Override
    public Long checkFileSize(String path) {
        // Get the input stream for the resource
        InputStream inputStream = getClass().getResourceAsStream("/" + path);

        if (inputStream == null) {
            System.err.println("Resource not found: " + path);
            return 0L;
        }

        try {
            // Read the resource into a byte array to get the size
            byte[] buffer = new byte[1024];
            long fileSize = 0L;
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileSize += bytesRead;
            }

            return fileSize;
        } catch (IOException e) {
            System.err.println("Error reading resource: " + e.getMessage());
            return 0L;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                System.err.println("Error closing InputStream: " + e.getMessage());
            }
        }
    }

    /**
     * Downloads a blob from Azure Blob Storage using the full URL including SAS token.
     *
     * @param link The full URL of the blob including SAS token.
     * @return InputStream containing the blob content.
     */
    public InputStream downloadBlob(String link) {
        try {
            // Create a BlobClient using the full URL including SAS token
            BlobClient blobClient = new BlobClientBuilder().endpoint(link).buildClient();
            return blobClient.openInputStream();
        } catch (Exception e) {
            logger.error("Error downloading blob from Azure Blob Storage: " + link, e);
            return null;
        }
    }

    /**
     * Retrieves the list of resource files from the specified path.
     *
     * @param path the directory path
     * @return a list of file names
     * @throws IOException if an I/O error occurs
     */
    private List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        // Read the list of files from the specified path
        try (InputStream in = getResourceAsStream(path); BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;
            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    /**
     * Retrieves an InputStream for the specified resource.
     *
     * @param resource the resource path
     * @return an InputStream for the resource
     */
    private InputStream getResourceAsStream(String resource) {
        final InputStream in = getContextClassLoader().getResourceAsStream(resource);
        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    /**
     * Retrieves the context ClassLoader.
     *
     * @return the context ClassLoader
     */
    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
