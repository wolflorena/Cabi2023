package com.example.server.service;

import com.example.server.repository.DTOs.PdfInfoDTO;
import com.example.server.repository.entity.PdfDocument;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PdfDocumentService {
    boolean uploadPdfDocument(Long customerId, MultipartFile file) throws IOException;
    Optional<PdfDocument> getPdfDocument(Long fileId);
    List<PdfInfoDTO> getDocumentsByCustomer(Long customerId);
    ByteArrayResource viewPdfDocument(Long fileId);
}
