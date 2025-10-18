package com.company.inventory.inventario.controller;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.inventory.inventario.model.Category;
import com.company.inventory.inventario.response.CategoryResponseRest;
import com.company.inventory.inventario.services.ICategoryService;
import com.company.inventory.inventario.util.CategoryExcelExporter;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {

    @Autowired
    private ICategoryService service;

    /**
     * Get all categories
     * @return ResponseEntity<CategoryResponseRest>
     * author <Adson Sa>
     */
    @GetMapping("/categories")
    public ResponseEntity<CategoryResponseRest> searchCategories() {
        ResponseEntity<CategoryResponseRest> response = service.search();
        return response;
    }
    
    /**
     * Get by id
     * @param id
     * @return ResponseEntity<CategoryResponseRest>
     * author <Adson Sa>
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> searchCategoryById(@PathVariable Long id) {
        ResponseEntity<CategoryResponseRest> response = service.searchById(id);
        return response;
    }
     /**
     * Save category
     * @param category
     * @return Category
     * author <Adson Sa>
     */
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseRest> saveCategory(@RequestBody Category category) {
        ResponseEntity<CategoryResponseRest> response = service.save(category);
        return response;
    }
    /**
     * Update category
     * @param category
     * @param id
     * @return Category
     * author <Adson Sa>
     */
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> updateCategory(@RequestBody Category category, @PathVariable("id") Long id) {
        ResponseEntity<CategoryResponseRest> response = service.update(category, id);
        return response;
    }
    /**
     * delete category
     * @param id
     * author <Adson Sa>
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> deleteCategory(@PathVariable("id") Long id) {
        ResponseEntity<CategoryResponseRest> response = service.delete(id);
        return response;
    }

    /**
     * Export categories to Excel
     * @param response
     * @throws IOException
     * author <Adson Sa>
     */
    @GetMapping("/categories/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
       response.setContentType("application/octet-stream");

       String headerKey = "Content-Disposition";
       String headerValue = "attachment; filename=result_category.xlsx";
       response.setHeader(headerKey, headerValue);

       ResponseEntity<CategoryResponseRest> categoryResponse = service.search();

       CategoryExcelExporter excelExporter = new CategoryExcelExporter(
               categoryResponse.getBody().getCategoryResponse().getCategorys());
       excelExporter.export(response);
    }
}
