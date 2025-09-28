package com.company.inventory.inventario.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.inventario.dao.ICategoryDao;
import com.company.inventory.inventario.dao.IProductDao;
import com.company.inventory.inventario.model.Category;
import com.company.inventory.inventario.model.Product;
import com.company.inventory.inventario.response.ProductResponseRest;
import com.company.inventory.inventario.services.IProductService;
import com.company.inventory.inventario.util.Util;

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
    @Transactional
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
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> searchById(Long id) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
            Optional<Product> product = productDao.findById(id);
            if(product.isPresent()){
                byte[] imageDecompressed = Util.decompressZLib(product.get().getPicture());
                product.get().setPicture(imageDecompressed);
                list.add(product.get());
                response.getProductResponse().setProducts(list);
                response.setMetadata("Resposta OK", "00", "Resposta bem-sucedida");
            } else {
                response.setMetadata("Resposta NOK", "01", "Produto não encontrado");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Erro na reposta", "-1", "Erro ao buscar por id");
            e.printStackTrace(); // Para ver o erro completo no console
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> searchByName(String name) {
         ProductResponseRest response = new ProductResponseRest();
         List<Product> list = new ArrayList<>();
         List<Product> listAux = new ArrayList<>();

        try {
            listAux = productDao.findByNameContainingIgnoreCase(name);
            if(listAux.size() > 0){
                listAux.stream().forEach(product -> {
                    byte[] imageDecompressed = Util.decompressZLib(product.getPicture());
                    product.setPicture(imageDecompressed);
                    list.add(product);
                });
                response.getProductResponse().setProducts(list);
                response.setMetadata("Resposta OK", "00", "Resposta bem-sucedida");
            } else {
                response.setMetadata("Resposta NOK", "01", "Produto não encontrado");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Erro na reposta", "-1", "Erro ao buscar por producto por nome");
            e.printStackTrace(); // Para ver o erro completo no console
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> deleteById(Long id) {
        ProductResponseRest response = new ProductResponseRest();

        try {
            productDao.deleteById(id);
            response.setMetadata("Resposta OK", "00", "Produto excluído com sucesso");
        } catch (Exception e) {
            response.setMetadata("Erro na resposta", "-1", "Erro ao excluir produto");
            e.printStackTrace(); // Para ver o erro completo no console
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> search() {
         ProductResponseRest response = new ProductResponseRest();
         List<Product> list = new ArrayList<>();
         List<Product> listAux = new ArrayList<>();

        try {
            listAux = (List<Product>) productDao.findAll();
            if(listAux.size() > 0){
                listAux.stream().forEach(product -> {
                    byte[] imageDecompressed = Util.decompressZLib(product.getPicture());
                    product.setPicture(imageDecompressed);
                    list.add(product);
                });
                response.getProductResponse().setProducts(list);
                response.setMetadata("Resposta OK", "00", "Resposta bem-sucedida");
            } else {
                response.setMetadata("Resposta NOK", "01", "Produto não encontrado");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Erro na reposta", "-1", "Erro ao buscar por produtos");
            e.printStackTrace(); // Para ver o erro completo no console
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long id) {
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

            // search product to update
            Optional<Product> productSearch = productDao.findById(id);
            if (productSearch.isPresent()) {

                productSearch.get().setAccount(product.getAccount());
                productSearch.get().setCategory(product.getCategory());
                productSearch.get().setName(product.getName());
                productSearch.get().setPicture(product.getPicture());
                productSearch.get().setPrice(product.getPrice());

                Product productToUpdate = productDao.save(productSearch.get());
                if (productToUpdate != null) {
                    list.add(productToUpdate);
                    response.getProductResponse().setProducts(list);
                    response.setMetadata("Resposta OK", "00", "Produto atualizado com sucesso");
                } else {
                    response.setMetadata("Erro na resposta", "-1", "Produto não atualizado");
                    return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                response.setMetadata("Erro na resposta", "-1", "Produto não Atualizado - não encontrado");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception e) {
            e.printStackTrace(); // Usar printStackTrace() em vez de getStackTrace()
            response.setMetadata("Erro na resposta", "-1", "Erro interno: " + e.getMessage());
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
    
}
