package com.purplebits.emrd2.service.impl;

import com.purplebits.emrd2.dto.request_response.FileProcessingResponse;
import com.purplebits.emrd2.entity.FilePattern;
import com.purplebits.emrd2.exceptions.*;
import com.purplebits.emrd2.repositories.FilePatternRepository;
import com.purplebits.emrd2.service.FileProcessingService;
import com.purplebits.emrd2.util.ResponseMessages;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class FileProcessingServiceImpl implements FileProcessingService {

    @Autowired
    private FilePatternRepository filePatternRepository;
    @Autowired
    Environment environment;


@Value("${file.processing.renamed-path}")
private String renamedDirectoryPath;

    public FileProcessingResponse processFile(Long entityId, String barcode, MultipartFile file) {
        FilePattern pattern = filePatternRepository.findByEntityId(entityId)
                .orElseThrow(() -> new PatternNotFoundException(
                        environment.getProperty(ResponseMessages.PATTERN_NOT_FOUND)));

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            throw new InvalidFileException(
                    environment.getProperty(ResponseMessages.INVALID_FILE));
        }

        // Remove file extension
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        Map<String, String> metadata = extractMetadataFromFlexiblePattern(fileNameWithoutExtension, pattern.getPatternFormat());

        // Rename the file using barcode
        String renamedFileName = barcode + fileExtension;
        String renamedPath = renamedDirectoryPath + "/" + renamedFileName;

        saveFileToTargetDirectory(file, renamedPath);
        return new FileProcessingResponse(fileNameWithoutExtension, renamedFileName, metadata);
    }

    private Map<String, String> extractMetadataFromFlexiblePattern(String fileName, String patternFormat) {
        String delimiter = determineDelimiter(patternFormat);

        // Parse pattern and file name using the delimiter
        String[] patternParts = patternFormat.replaceAll("[{}]", "").split(Pattern.quote(delimiter));
        String[] fileParts = fileName.split(Pattern.quote(delimiter));

        if (patternParts.length != fileParts.length) {
            throw new FileNameNotMatchException(
                    environment.getProperty(ResponseMessages.FILE_NAME_DOES_NOT_MATCH_PATTERN));
        }

        Map<String, String> metadata = new HashMap<>();
        for (int i = 0; i < patternParts.length; i++) {
            metadata.put(patternParts[i], fileParts[i]);
        }
        return metadata;
    }

    private String determineDelimiter(String patternFormat) {
        if (patternFormat.contains("-")) {
            return "-";
        } else if (patternFormat.contains("_")) {
            return "_";
        } else if (patternFormat.contains(" ")) {
            return " ";
        } else {
            throw new UnsupportedDelimiterException(
                    environment.getProperty(ResponseMessages.UNSUPPORTED_DELIMITER_IN_PATTERN));
        }
    }


    private void saveFileToTargetDirectory(MultipartFile file, String targetPath) {
        try {
            File targetDir = new File(renamedDirectoryPath);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            file.transferTo(new File(targetPath));
        } catch (IOException ex) {
            throw new FileSavingException(
                    environment.getProperty(ResponseMessages.ERROR_SAVING_FILE));
        }
    }

}
