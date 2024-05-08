package com.example.server.repository.DTOs;

import com.example.server.repository.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPageDTO {
    private int total;
    private Page<Inventory> pagedInventory;
}
