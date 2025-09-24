package com.company.inventory.inventario.services;

import org.springframework.http.ResponseEntity;

import com.company.inventory.inventario.model.Product;
import com.company.inventory.inventario.response.ProductResponseRest;

public interface IProductService {
    // CRUD method signatures for Product entity
    public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId);
    public ResponseEntity<ProductResponseRest> searchById(Long id);
    // public ResponseEntity<ProductResponseRest> update(Product product, Long id);
    // public ResponseEntity<ProductResponseRest> delete(Long id);
}
