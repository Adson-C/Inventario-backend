package com.company.inventory.inventario.dao;

import org.springframework.data.repository.CrudRepository;

import com.company.inventory.inventario.model.Product;

public interface IProductDao extends CrudRepository<Product, Long> {
    
}
