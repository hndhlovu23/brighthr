package com.brighthr.exam.cmdlinerunner;

import com.brighthr.exam.domain.File;
import com.brighthr.exam.domain.Filetype;
import com.brighthr.exam.domain.Folder;
import com.brighthr.exam.repository.FileRepository;
import com.brighthr.exam.repository.FiletypeRepository;
import com.brighthr.exam.repository.FolderRepository;
import com.brighthr.exam.service.azurefileupload.AzureFileService;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
@Order(1)
public class FileCommandLineRunner implements CommandLineRunner {

    protected final Log logger = LogFactory.getLog(getClass());

    private final String jsonDummyData = "testfilesjson/dummy-data.json";

    private final String containerName = "testfiles";

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FiletypeRepository filetypeRepository;

    @Autowired
    private AzureFileService azureFileUploadService;

    @Override
    public void run(String... strings) throws Exception {
        //Delete files in database
        deleteExistingFiles();

        //Load files to database
        loadFiles();
    }

    private void loadFiles() {
        JSONArray jsonArray = null;

        byte[] fileContent = new byte[0];

        try {
            fileContent = FileCopyUtils.copyToByteArray(new ClassPathResource(jsonDummyData).getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();

        try {
            jsonArray = (JSONArray) parser.parse(new String(fileContent));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Object o : jsonArray) {
            JSONObject jsonTestFiles = (JSONObject) o;

            String name = (String) jsonTestFiles.get("name");
            JSONArray filesArray = (JSONArray) jsonTestFiles.get("files");

            Folder folder = folderRepository.findByFolderName(name);

            if (folder == null) {
                folder = new Folder();
                folder.setName(name);
                folder = folderRepository.save(folder);
            }

            for (Object fileObj : filesArray) {
                JSONObject jsonFile = (JSONObject) fileObj;

                String fileName = (String) jsonFile.get("name");
                String filetypeName = (String) jsonFile.get("type");
                String added = (String) jsonFile.get("added");

                String filelocation = (String) jsonFile.get("filelocation");

                Date addedDate = null;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                try {
                    LocalDate localDate = LocalDate.parse(added, formatter);
                    addedDate = Date.valueOf(localDate);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }

                String fileAzureLink = azureFileUploadService.uploadFileUpload(containerName, filelocation);

                Long fileSize = azureFileUploadService.checkFileSize(filelocation);

                Filetype filetype = filetypeRepository.findByFiletypeName(filetypeName);

                if (filetype == null) {
                    filetype = new Filetype();
                    filetype.setTypeName(filetypeName);
                    filetype = filetypeRepository.save(filetype);
                }

                File file = new File();
                file.setName(fileName);
                file.setFiletype(filetype);
                file.setSize(fileSize);
                file.setFolder(folder);
                file.setFileUrl(fileAzureLink);
                file.setAdded(addedDate);

                file = fileRepository.save(file);

                logger.info("File: " + file);
            }
        }
    }

    private void deleteExistingFiles() {
        // Delete all files in database
        fileRepository.deleteAll();

        //Delete all file types in database
        filetypeRepository.deleteAll();

        //Delete all folders in database
        folderRepository.deleteAll();
    }
}
