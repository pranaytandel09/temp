package com.purplebits.emrd2.controller;

import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.FileProcessingResponse;
import com.purplebits.emrd2.exceptions.*;
import com.purplebits.emrd2.service.FileProcessingService;
import com.purplebits.emrd2.util.ResponseMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileProcessingService fileProcessingService;
    @Autowired
    Environment environment;
    private final Logger logger = LogManager.getLogger(FileController.class);

//    @PostMapping("/process")
//    public ResponseEntity<FileProcessingResponse> processFile(
//            @RequestParam("entityId") Long entityId,
//            @RequestParam("barcode") String barcode,
//            @RequestParam("file") MultipartFile file) {
//        FileProcessingResponse response = fileProcessingService.processFile(entityId, barcode, file);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/process")
    public ResponseEntity<?> processFile(
            @RequestParam("entityId") Long entityId,
            @RequestParam("barcode") String barcode,
            @RequestParam("file") MultipartFile file) {
        try {
            FileProcessingResponse response = fileProcessingService.processFile(entityId, barcode, file);
            return ResponseEntity.ok(response);
        } catch (PatternNotFoundException e) {
            logger.error("Pattern not found: {}", e.getMessage());
            return buildErrorResponse(ResponseMessages.PATTERN_NOT_FOUND, ResponseCode.NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (InvalidFileException e) {
            logger.error("Invalid file: {}", e.getMessage());
            return buildErrorResponse(ResponseMessages.INVALID_FILE, ResponseCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        } catch (FileNameNotMatchException e) {
            logger.error("File name does not match pattern: {}", e.getMessage());
            return buildErrorResponse(ResponseMessages.FILE_NAME_DOES_NOT_MATCH_PATTERN, ResponseCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedDelimiterException e) {
            logger.error("Unsupported delimiter: {}", e.getMessage());
            return buildErrorResponse(ResponseMessages.UNSUPPORTED_DELIMITER_IN_PATTERN, ResponseCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        } catch (FileSavingException e) {
            logger.error("Error saving file: {}", e.getMessage());
            return buildErrorResponse(ResponseMessages.ERROR_SAVING_FILE, ResponseCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return buildErrorResponse("unexpected.error", ResponseCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Response<Object>> buildErrorResponse(String messageKey, Integer code, HttpStatus status) {
        Response<Object> response = new Response<>();
        response.setCode(code);
        response.setMessage(environment.getProperty(messageKey));
        return ResponseEntity.status(status).body(response);
    }


}
