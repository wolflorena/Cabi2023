package com.example.server.service;

import com.example.server.repository.DTOs.InventoryPageDTO;
import com.example.server.repository.DTOs.InventoryUpdateDTO;
import com.example.server.repository.entity.Inventory;
import org.springframework.data.domain.Pageable;

public interface InventoryService {
    Inventory addProduct(Inventory inventory);
    Inventory getById(Long inventoryId);
    InventoryPageDTO getInventoryPaginated(Pageable pageable);
    Boolean deleteProduct(Long inventoryId);
    Inventory updateProduct(Long inventoryId, InventoryUpdateDTO inventory);
}
