package com.example.server.controller;

import com.example.server.repository.DTOs.PdfInfoDTO;
import com.example.server.repository.entity.PdfDocument;
import com.example.server.service.PdfDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/documents")
public class PdfDocumentController {
    @Autowired
    private PdfDocumentService pdfDocumentService;

    public PdfDocumentController(PdfDocumentService pdfDocumentService) {
        this.pdfDocumentService = pdfDocumentService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean uploadPdfDocument(@RequestParam Long customerId, @RequestParam("file") MultipartFile file) throws IOException {
        return pdfDocumentService.uploadPdfDocument(customerId, file);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadPdfDocument(@PathVariable Long fileId) {
        Optional<PdfDocument> pdfDocumentOptional = pdfDocumentService.getPdfDocument(fileId);

        if (!pdfDocumentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        PdfDocument pdfDocument = pdfDocumentOptional.get();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfDocument.getFileName() + "\"")
                .body(new ByteArrayResource(pdfDocument.getData()));
    }

    @GetMapping("/patient/{customerId}")
    public List<PdfInfoDTO> getPdfDocumentsByPatient(@PathVariable Long customerId) {
        return pdfDocumentService.getDocumentsByCustomer(customerId);
    }

    @GetMapping("/view/{fileId}")
    public void viewPdfDocument(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        ByteArrayResource pdfDocument = pdfDocumentService.viewPdfDocument(fileId);

        if (pdfDocument == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.getOutputStream().write(pdfDocument.getByteArray());
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<Void> deletePdfDocument(@PathVariable Long fileId) {
        boolean isDeleted = pdfDocumentService.deletePdfDocument(fileId);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
