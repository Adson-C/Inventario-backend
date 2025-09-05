package com.company.inventory.inventario.services;

import org.springframework.http.ResponseEntity;

import com.company.inventory.inventario.response.CategoryResponseRest;

public interface ICategoryService {

    public ResponseEntity<CategoryResponseRest> search();
    public ResponseEntity<CategoryResponseRest> searchById(Long id);
}
