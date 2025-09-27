package com.company.inventory.inventario.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.inventario.model.Product;
import com.company.inventory.inventario.response.ProductResponseRest;
import com.company.inventory.inventario.services.IProductService;
import com.company.inventory.inventario.util.Util;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {

    private IProductService productService;

    public ProductRestController(IProductService productService) {
        this.productService = productService;
    }

    /**
     * Save a product with image
     * @param picture
     * @param name
     * @param price
     * @param account
     * @param categoryId
     * @return
     * @throws IOException
     */
    @PostMapping("/products")
    public ResponseEntity<ProductResponseRest> save(
                  @RequestParam("picture") MultipartFile picture,
                    @RequestParam("name") String name,
                    @RequestParam("price") int price,
                    @RequestParam("account") int account,
                    @RequestParam("categoryId") Long categoryId) throws IOException
    {
        // Validate image size (10MB max)
        if (picture.getSize() > 10 * 1024 * 1024) {
            ProductResponseRest errorResponse = new ProductResponseRest();
            errorResponse.setMetadata("Erro na validação", "-1", "Imagem muito grande. Tamanho máximo: 10MB");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Validate image type
        String contentType = picture.getContentType();
        if (contentType == null || (!contentType.startsWith("image/"))) {
            ProductResponseRest errorResponse = new ProductResponseRest();
            errorResponse.setMetadata("Erro na validação", "-1", "Arquivo deve ser uma imagem válida");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Product product = new Product();
        product.setName(name);
        product.setAccount(account);
        product.setPrice(price);
        
        // Compress image before saving
        byte[] compressedPicture = Util.compressZLib(picture.getBytes());
        product.setPicture(compressedPicture);

        ResponseEntity<ProductResponseRest> response = productService.save(product, categoryId);

        return response;
    }
      /**
        * search a product by ID
        * @param id
        * @return
       
        */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> searchById(@PathVariable("id") Long id) {
        ResponseEntity<ProductResponseRest> response = productService.searchById(id);
        return response;
    }

    /**
     * search products by name (case insensitive, partial match)
     * @param name
     * @return
     */
    @GetMapping("/products/filter/{name}")
    public ResponseEntity<ProductResponseRest> searchByName(@PathVariable("name") String name) {
        ResponseEntity<ProductResponseRest> response = productService.searchByName(name);
        return response;
    }

    /**
     * delete a product by ID
     * @param id
     * @return
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> deleteById(@PathVariable("id") Long id) {
        ResponseEntity<ProductResponseRest> response = productService.deleteById(id);
        return response;
    }
}
