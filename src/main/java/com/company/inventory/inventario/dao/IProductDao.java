package com.company.inventory.inventario.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.company.inventory.inventario.model.Product;

public interface IProductDao extends CrudRepository<Product, Long> {
    
    // consulta personalizada JpaQL
    @Query("select p from Product p where p.name like %?1%")
    List<Product> findByNameLike(String name);

    List<Product> findByNameContainingIgnoreCase(String name);

}
