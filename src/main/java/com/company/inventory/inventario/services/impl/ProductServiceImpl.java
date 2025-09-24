package com.company.inventory.inventario.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.company.inventory.inventario.dao.ICategoryDao;
import com.company.inventory.inventario.dao.IProductDao;
import com.company.inventory.inventario.model.Category;
import com.company.inventory.inventario.model.Product;
import com.company.inventory.inventario.response.ProductResponseRest;
import com.company.inventory.inventario.services.IProductService;

@Service
public class ProductServiceImpl implements IProductService {

    private ICategoryDao categoryDao;
    private IProductDao productDao;


    public ProductServiceImpl(ICategoryDao categoryDao, IProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }
    // Save a product
    @Override
    public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
        // Implementation logic to save the product
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
            Optional<Category> category = categoryDao.findById(categoryId);
            if (category.isPresent()) {
                product.setCategory(category.get());
            } else {
                response.setMetadata("Erro na reposta", "-1", "Categoria não encontrada");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

            Product productSaved = productDao.save(product);
            if (productSaved != null) {
                list.add(productSaved);
                response.getProductResponse().setProducts(list);
                response.setMetadata("Resposta OK", "00", "Produto salvo com sucesso");
            } else {
                response.setMetadata("Erro na resposta", "-1", "Produto não salvo");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
            }
            
        } catch (Exception e) {
            e.printStackTrace(); // Usar printStackTrace() em vez de getStackTrace()
            response.setMetadata("Erro na resposta", "-1", "Erro interno: " + e.getMessage());
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
    
}
