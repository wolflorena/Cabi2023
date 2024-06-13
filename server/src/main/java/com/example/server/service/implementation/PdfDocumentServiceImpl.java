package com.example.server.service.implementation;

import com.example.server.exception.types.NotFoundException;
import com.example.server.repository.CustomerRepository;
import com.example.server.repository.DTOs.PdfInfoDTO;
import com.example.server.repository.PdfDocumentRepository;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.PdfDocument;
import com.example.server.service.PdfDocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PdfDocumentServiceImpl implements PdfDocumentService {

    private PdfDocumentRepository pdfDocumentRepository;
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    public PdfDocumentServiceImpl(PdfDocumentRepository pdfDocumentRepository,
                                  CustomerRepository customerRepository,
                                  ModelMapper modelMapper) {
        this.pdfDocumentRepository = pdfDocumentRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean uploadPdfDocument(Long customerId, MultipartFile file) throws IOException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));

        PdfDocument pdfDocument = new PdfDocument();
        pdfDocument.setFileName(file.getOriginalFilename());
        pdfDocument.setData(file.getBytes());
        pdfDocument.setCustomer(customer);

        PdfDocument savedDocument = pdfDocumentRepository.save(pdfDocument);

        return savedDocument != null;
    }

    @Override
    public Optional<PdfDocument> getPdfDocument(Long fileId) {
        return pdfDocumentRepository.findById(fileId);
    }

    @Override
    public List<PdfInfoDTO> getDocumentsByCustomer(Long customerId) {
        List<PdfDocument> pdfDocumentList = pdfDocumentRepository.findByCustomerId(customerId);

        return pdfDocumentList.stream().map(pdfDocument -> modelMapper.map(pdfDocument, PdfInfoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ByteArrayResource viewPdfDocument(Long fileId) {
        PdfDocument pdfDocument = pdfDocumentRepository.findById(fileId).orElseThrow(() -> new NotFoundException("Document not found"));

        return new ByteArrayResource(pdfDocument.getData());
    }
}
