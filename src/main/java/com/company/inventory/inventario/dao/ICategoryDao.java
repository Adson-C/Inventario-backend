package com.company.inventory.inventario.dao;

import org.springframework.data.repository.CrudRepository;

import com.company.inventory.inventario.model.Category;

public interface ICategoryDao extends CrudRepository<Category, Long> {
    
}
