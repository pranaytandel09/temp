package com.purplebits.emrd2.service;

import com.purplebits.emrd2.dto.request_response.FileProcessingResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileProcessingService {
    FileProcessingResponse processFile(Long entityId, String barcode, MultipartFile file);
}

