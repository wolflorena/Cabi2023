package com.example.server.service.implementation;

import com.example.server.exception.types.NotFoundException;
import com.example.server.exception.types.ProductExistsException;
import com.example.server.repository.DTOs.InventoryPageDTO;
import com.example.server.repository.DTOs.InventoryUpdateDTO;
import com.example.server.repository.InventoryRepository;
import com.example.server.repository.entity.Inventory;
import com.example.server.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    private final ModelMapper modelMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, ModelMapper modelMapper) {
        this.inventoryRepository = inventoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Inventory addProduct(Inventory inventory) {
        inventoryRepository.save(inventory);
        return inventory;
    }

    @Override
    public Inventory getById(Long inventoryId) {
        return inventoryRepository.findById(inventoryId).orElseThrow(()-> new NotFoundException("Product not found"));
    }

    @Override
    public Boolean deleteProduct(Long inventoryId) {
        inventoryRepository.deleteById(inventoryId);
        return true;
    }

    @Override
    public InventoryPageDTO getInventoryPaginated(Pageable pageable) {
        InventoryPageDTO inventoryPageDTO = new InventoryPageDTO();
        Page<Inventory> products = inventoryRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        
        inventoryPageDTO.setTotal((int) products.getTotalElements());

        List<Inventory> result = products.getContent()
                .stream()
                .map(inventory -> modelMapper.map(inventory, Inventory.class))
                .toList();
        return new InventoryPageDTO(inventoryPageDTO.getTotal(), new PageImpl<>(result, pageable, result.size()));
    }

    @Override
    public Inventory updateProduct(Long inventoryId, InventoryUpdateDTO inventoryUpdateDTO) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow();
        if (inventoryUpdateDTO.getProduct() != null) {
            if (inventoryRepository.findByProduct(inventoryUpdateDTO.getProduct()).isEmpty()) {
                inventory.setProduct(inventoryUpdateDTO.getProduct());
            } else {
                throw new ProductExistsException("This product already exists");
            }
        }
        if (inventoryUpdateDTO.getQuantity() != 0) {
            inventory.setQuantity(inventoryUpdateDTO.getQuantity());
        }

        inventoryRepository.save(inventory);
        return modelMapper.map(inventory, Inventory.class);
    }
}
