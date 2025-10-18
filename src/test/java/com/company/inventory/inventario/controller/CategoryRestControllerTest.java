package com.company.inventory.inventario.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.company.inventory.inventario.model.Category;
import com.company.inventory.inventario.response.CategoryResponseRest;
import com.company.inventory.inventario.services.ICategoryService;

@ExtendWith(MockitoExtension.class)
class CategoryRestControllerTest {

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryRestController categoryRestController;

    private MockMvc mockMvc;

    private Category testCategory;
    private CategoryResponseRest testResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryRestController).build();
        
        // Setup test data
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");

        testResponse = new CategoryResponseRest();
        testResponse.setMetadata("OK", "00", "Success");
    }

    @Test
    void testSearchCategories_Success() throws Exception {
        // Given
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.ok(testResponse);
        when(categoryService.search()).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(categoryService, times(1)).search();
    }

    @Test
    void testSearchCategoryById_Success() throws Exception {
        // Given
        Long categoryId = 1L;
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.ok(testResponse);
        when(categoryService.searchById(categoryId)).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(get("/api/v1/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(categoryService, times(1)).searchById(categoryId);
    }

    @Test
    void testSearchCategoryById_NotFound() throws Exception {
        // Given
        Long categoryId = 999L;
        CategoryResponseRest errorResponse = new CategoryResponseRest();
        errorResponse.setMetadata("ERROR", "01", "Category not found");
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        when(categoryService.searchById(categoryId)).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(get("/api/v1/categories/{id}", categoryId))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).searchById(categoryId);
    }

    @Test
    void testSaveCategory_Success() throws Exception {
        // Given
        String categoryJson = """
            {
                "name": "New Category",
                "description": "New Description"
            }
            """;
        
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(testResponse);
        when(categoryService.save(any(Category.class))).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isCreated());

        verify(categoryService, times(1)).save(any(Category.class));
    }

    @Test
    void testSaveCategory_ValidationError() throws Exception {
        // Given
        String invalidCategoryJson = """
            {
                "name": "",
                "description": "Description"
            }
            """;
        
        CategoryResponseRest errorResponse = new CategoryResponseRest();
        errorResponse.setMetadata("ERROR", "02", "Validation failed");
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.badRequest().body(errorResponse);
        when(categoryService.save(any(Category.class))).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidCategoryJson))
                .andExpect(status().isBadRequest());

        verify(categoryService, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_Success() throws Exception {
        // Given
        Long categoryId = 1L;
        String categoryJson = """
            {
                "name": "Updated Category",
                "description": "Updated Description"
            }
            """;
        
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.ok(testResponse);
        when(categoryService.update(any(Category.class), eq(categoryId))).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(put("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).update(any(Category.class), eq(categoryId));
    }

    @Test
    void testUpdateCategory_NotFound() throws Exception {
        // Given
        Long categoryId = 999L;
        String categoryJson = """
            {
                "name": "Updated Category",
                "description": "Updated Description"
            }
            """;
        
        CategoryResponseRest errorResponse = new CategoryResponseRest();
        errorResponse.setMetadata("ERROR", "01", "Category not found");
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        when(categoryService.update(any(Category.class), eq(categoryId))).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(put("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).update(any(Category.class), eq(categoryId));
    }

    @Test
    void testDeleteCategory_Success() throws Exception {
        // Given
        Long categoryId = 1L;
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.ok(testResponse);
        when(categoryService.delete(categoryId)).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).delete(categoryId);
    }

    @Test
    void testDeleteCategory_NotFound() throws Exception {
        // Given
        Long categoryId = 999L;
        CategoryResponseRest errorResponse = new CategoryResponseRest();
        errorResponse.setMetadata("ERROR", "01", "Category not found");
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        when(categoryService.delete(categoryId)).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).delete(categoryId);
    }

    @Test
    void testExportToExcel_Success() throws Exception {
        // Given
        List<Category> categories = new ArrayList<>();
        categories.add(testCategory);
        
        CategoryResponseRest responseRest = new CategoryResponseRest();
        responseRest.setMetadata("OK", "00", "Success");
        // Note: You would need to set the categoryResponse with the categories list
        // This is a simplified test - in a real scenario, you'd need to properly set up the response structure
        
        ResponseEntity<CategoryResponseRest> responseEntity = ResponseEntity.ok(responseRest);
        when(categoryService.search()).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(get("/api/v1/categories/export/excel"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=result_category.xlsx"));

        verify(categoryService, times(1)).search();
    }


    // Unit tests for direct method calls
    @Test
    void testSearchCategories_DirectCall() {
        // Given
        ResponseEntity<CategoryResponseRest> expectedResponse = ResponseEntity.ok(testResponse);
        when(categoryService.search()).thenReturn(expectedResponse);

        // When
        ResponseEntity<CategoryResponseRest> actualResponse = categoryRestController.searchCategories();

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).search();
    }

    @Test
    void testSearchCategoryById_DirectCall() {
        // Given
        Long categoryId = 1L;
        ResponseEntity<CategoryResponseRest> expectedResponse = ResponseEntity.ok(testResponse);
        when(categoryService.searchById(categoryId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<CategoryResponseRest> actualResponse = categoryRestController.searchCategoryById(categoryId);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).searchById(categoryId);
    }

    @Test
    void testSaveCategory_DirectCall() {
        // Given
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        
        ResponseEntity<CategoryResponseRest> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(testResponse);
        when(categoryService.save(category)).thenReturn(expectedResponse);

        // When
        ResponseEntity<CategoryResponseRest> actualResponse = categoryRestController.saveCategory(category);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).save(category);
    }

    @Test
    void testUpdateCategory_DirectCall() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setName("Updated Category");
        category.setDescription("Updated Description");
        
        ResponseEntity<CategoryResponseRest> expectedResponse = ResponseEntity.ok(testResponse);
        when(categoryService.update(category, categoryId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<CategoryResponseRest> actualResponse = categoryRestController.updateCategory(category, categoryId);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).update(category, categoryId);
    }

    @Test
    void testDeleteCategory_DirectCall() {
        // Given
        Long categoryId = 1L;
        ResponseEntity<CategoryResponseRest> expectedResponse = ResponseEntity.ok(testResponse);
        when(categoryService.delete(categoryId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<CategoryResponseRest> actualResponse = categoryRestController.deleteCategory(categoryId);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(categoryService, times(1)).delete(categoryId);
    }
}
