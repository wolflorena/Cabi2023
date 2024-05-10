package com.example.server.controller;

import com.example.server.repository.DTOs.InventoryPageDTO;
import com.example.server.repository.DTOs.InventoryUpdateDTO;
import com.example.server.repository.entity.Inventory;
import com.example.server.service.InventoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
        private final InventoryService inventoryService;

        public InventoryController(InventoryService inventoryService) {
                this.inventoryService = inventoryService;
        }

        @PostMapping
        public ResponseEntity<Object> addService(@RequestBody Inventory inventory){
                return new ResponseEntity<>(inventoryService.addProduct(inventory), HttpStatus.OK);
        }

        @GetMapping("/allPage")
        public ResponseEntity<InventoryPageDTO> getInventoryPaginated(@RequestParam(required = true) int pageSize,
                                                                    @RequestParam(required = true) int pageNumber) {
                return new ResponseEntity<>(
                        inventoryService.getInventoryPaginated(
                                PageRequest.of(
                                        pageNumber,
                                        pageSize
                                )
                        ),
                        HttpStatus.OK
                );
        }

        @GetMapping
        public ResponseEntity<Inventory> getProduct(@RequestParam Long inventoryId) {
                return new ResponseEntity<>(inventoryService.getById(inventoryId), HttpStatus.OK);
        }

        @DeleteMapping
        public ResponseEntity<Boolean> deleteProduct(@RequestParam Long inventoryId) {
                return new ResponseEntity<>(inventoryService.deleteProduct(inventoryId), HttpStatus.OK);
        }

        @PatchMapping("/update-product")
        public Inventory updateProduct(@RequestParam Long inventoryId,@RequestBody InventoryUpdateDTO inventory) {
                return inventoryService.updateProduct(inventoryId, inventory);
        }
}
