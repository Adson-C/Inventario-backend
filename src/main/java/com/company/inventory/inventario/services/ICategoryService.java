package com.company.inventory.inventario.services;

import org.springframework.http.ResponseEntity;

import com.company.inventory.inventario.model.Category;
import com.company.inventory.inventario.response.CategoryResponseRest;

public interface ICategoryService {

    // Métodos para buscar categorias
    public ResponseEntity<CategoryResponseRest> search();
    // Método para buscar categoria por ID
    public ResponseEntity<CategoryResponseRest> searchById(Long id);
    // Método para criar nova categoria
    public ResponseEntity<CategoryResponseRest> save(Category category);
    // // Método para atualizar categoria
    public ResponseEntity<CategoryResponseRest> update(Category category, Long id);
    // // Método para excluir categoria
    public ResponseEntity<CategoryResponseRest> delete(Long id);
}
