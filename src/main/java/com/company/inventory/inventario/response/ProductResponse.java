package com.company.inventory.inventario.response;

import java.util.List;

import com.company.inventory.inventario.model.Product;

import lombok.Data;
@Data
public class ProductResponse {
    
    private List<Product> products;
}
