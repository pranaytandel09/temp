package com.purplebits.emrd2.controller;

import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.BarcodeRequest;
import com.purplebits.emrd2.exceptions.EntityNotFoundException;
import com.purplebits.emrd2.exceptions.ProjectNotFoundException;
import com.purplebits.emrd2.service.impl.BarcodeService;

import com.purplebits.emrd2.util.ResponseMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/barcodes")
public class BarcodeController {

    @Autowired
    private BarcodeService barcodeService;
    @Autowired
    Environment environment;
    private final Logger logger = LogManager.getLogger(BarcodeController.class);


    @PostMapping("/generate")
    public ResponseEntity<Response<PaginationResponseDTO<String>>> generateBarcodes(@RequestBody @Validated Request<BarcodeRequest> request) {
        Response<PaginationResponseDTO<String>> response = new Response<>();
        BarcodeRequest query = request.getQuery();
        try {
            PaginationResponseDTO<String> barcodes = barcodeService.
                    generateBarcodes(query.getEntityId(), query.getProjectId(), query.getCount());
            response.setCode(ResponseCode.OK);
            response.setMessage("Barcodes generated successfully.");
            response.setResult(barcodes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (
                EntityNotFoundException e) {
            response.setCode(ResponseCode.NOT_FOUND);
            response.setMessage(environment.getProperty(ResponseMessages.ENTITY_NOT_EXIST));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (ProjectNotFoundException e) {
            response.setCode(ResponseCode.NOT_FOUND);
            response.setMessage(environment.getProperty(ResponseMessages.PROJECT_NOT_EXIST));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error during barcode generation: {}", e.getMessage(), e);
            response.setCode(ResponseCode.INTERNAL_SERVER_ERROR);
            response.setMessage(environment.getProperty("unexpected.error"));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
