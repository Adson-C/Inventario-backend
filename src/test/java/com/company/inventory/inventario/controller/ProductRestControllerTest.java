package com.company.inventory.inventario.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.company.inventory.inventario.model.Product;
import com.company.inventory.inventario.response.ProductResponseRest;
import com.company.inventory.inventario.services.IProductService;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductRestController productRestController;

    private MockMvc mockMvc;

    private Product testProduct;
    private ProductResponseRest testResponse;
    private MockMultipartFile testImageFile;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();
        
        // Setup test data
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(100);
        testProduct.setAccount(10);
        testProduct.setPicture(new byte[]{1, 2, 3, 4, 5});

        testResponse = new ProductResponseRest();
        testResponse.setMetadata("OK", "00", "Success");

        // Create a test image file
        testImageFile = new MockMultipartFile(
                "picture",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    @Test
    void testSaveProduct_Success() throws Exception {
        // Given
        ResponseEntity<ProductResponseRest> responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(testResponse);
        when(productService.save(any(Product.class), anyLong())).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(multipart("/api/v1/products")
                .file(testImageFile)
                .param("name", "Test Product")
                .param("price", "100")
                .param("account", "10")
                .param("categoryId", "1"))
                .andExpect(status().isCreated());

        verify(productService, times(1)).save(any(Product.class), eq(1L));
    }

    @Test
    void testSaveProduct_ImageTooLarge() throws Exception {
        // Given
        MockMultipartFile largeImageFile = new MockMultipartFile(
                "picture",
                "large-image.jpg",
                "image/jpeg",
                new byte[11 * 1024 * 1024] // 11MB - exceeds 10MB limit
        );

        // When & Then
        mockMvc.perform(multipart("/api/v1/products")
                .file(largeImageFile)
                .param("name", "Test Product")
                .param("price", "100")
                .param("account", "10")
                .param("categoryId", "1"))
                .andExpect(status().isBadRequest());

        verify(productService, never()).save(any(Product.class), anyLong());
    }

    @Test
    void testSaveProduct_InvalidImageType() throws Exception {
        // Given
        MockMultipartFile invalidFile = new MockMultipartFile(
                "picture",
                "test.txt",
                "text/plain",
                "not an image".getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/api/v1/products")
                .file(invalidFile)
                .param("name", "Test Product")
                .param("price", "100")
                .param("account", "10")
                .param("categoryId", "1"))
                .andExpect(status().isBadRequest());

        verify(productService, never()).save(any(Product.class), anyLong());
    }

    @Test
    void testSaveProduct_ServiceError() throws Exception {
        // Given
        ProductResponseRest errorResponse = new ProductResponseRest();
        errorResponse.setMetadata("ERROR", "01", "Service error");
        ResponseEntity<ProductResponseRest> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        when(productService.save(any(Product.class), anyLong())).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(multipart("/api/v1/products")
                .file(testImageFile)
                .param("name", "Test Product")
                .param("price", "100")
                .param("account", "10")
                .param("categoryId", "1"))
                .andExpect(status().isInternalServerError());

        verify(productService, times(1)).save(any(Product.class), eq(1L));
    }

    @Test
    void testSearchProductById_Success() throws Exception {
        // Given
        Long productId = 1L;
        ResponseEntity<ProductResponseRest> responseEntity = ResponseEntity.ok(testResponse);
        when(productService.searchById(productId)).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(get("/api/v1/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(productService, times(1)).searchById(productId);
    }

    @Test
    void testSearchProductById_NotFound() throws Exception {
        // Given
        Long productId = 999L;
        ProductResponseRest errorResponse = new ProductResponseRest();
        errorResponse.setMetadata("ERROR", "01", "Product not found");
        ResponseEntity<ProductResponseRest> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        when(productService.searchById(productId)).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(get("/api/v1/products/{id}", productId))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).searchById(productId);
    }

    @Test
    void testSearchProductByName_Success() throws Exception {
        // Given
        String productName = "Test Product";
        ResponseEntity<ProductResponseRest> responseEntity = ResponseEntity.ok(testResponse);
        when(productService.searchByName(productName)).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(get("/api/v1/products/filter/{name}", productName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(productService, times(1)).searchByName(productName);
    }

    @Test
    void testSearchProducts_Success() throws Exception {
        // Given
        ResponseEntity<ProductResponseRest> responseEntity = ResponseEntity.ok(testResponse);
        when(productService.search()).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(productService, times(1)).search();
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        // Given
        Long productId = 1L;
        ResponseEntity<ProductResponseRest> responseEntity = ResponseEntity.ok(testResponse);
        when(productService.deleteById(productId)).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        // Given
        Long productId = 999L;
        ProductResponseRest errorResponse = new ProductResponseRest();
        errorResponse.setMetadata("ERROR", "01", "Product not found");
        ResponseEntity<ProductResponseRest> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        when(productService.deleteById(productId)).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).deleteById(productId);
    }



    @Test
    void testExportToExcel_Success() throws Exception {
        // Given
        List<Product> products = new ArrayList<>();
        products.add(testProduct);
        
        ProductResponseRest responseRest = new ProductResponseRest();
        responseRest.setMetadata("OK", "00", "Success");
        // Note: You would need to set the productResponse with the products list
        // This is a simplified test - in a real scenario, you'd need to properly set up the response structure
        
        ResponseEntity<ProductResponseRest> responseEntity = ResponseEntity.ok(responseRest);
        when(productService.search()).thenReturn(responseEntity);

        // When & Then
        mockMvc.perform(get("/api/v1/products/export/excel"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=result_product.xlsx"));

        verify(productService, times(1)).search();
    }

    // Unit tests for direct method calls
    @Test
    void testSaveProduct_DirectCall() throws IOException {
        // Given
        ResponseEntity<ProductResponseRest> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(testResponse);
        when(productService.save(any(Product.class), eq(1L))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ProductResponseRest> actualResponse = productRestController.save(
                testImageFile, "Test Product", 100, 10, 1L);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).save(any(Product.class), eq(1L));
    }

    @Test
    void testSearchProductById_DirectCall() {
        // Given
        Long productId = 1L;
        ResponseEntity<ProductResponseRest> expectedResponse = ResponseEntity.ok(testResponse);
        when(productService.searchById(productId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ProductResponseRest> actualResponse = productRestController.searchById(productId);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).searchById(productId);
    }

    @Test
    void testSearchProductByName_DirectCall() {
        // Given
        String productName = "Test Product";
        ResponseEntity<ProductResponseRest> expectedResponse = ResponseEntity.ok(testResponse);
        when(productService.searchByName(productName)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ProductResponseRest> actualResponse = productRestController.searchByName(productName);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).searchByName(productName);
    }

    @Test
    void testSearchProducts_DirectCall() {
        // Given
        ResponseEntity<ProductResponseRest> expectedResponse = ResponseEntity.ok(testResponse);
        when(productService.search()).thenReturn(expectedResponse);

        // When
        ResponseEntity<ProductResponseRest> actualResponse = productRestController.search();

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).search();
    }

    @Test
    void testDeleteProduct_DirectCall() {
        // Given
        Long productId = 1L;
        ResponseEntity<ProductResponseRest> expectedResponse = ResponseEntity.ok(testResponse);
        when(productService.deleteById(productId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ProductResponseRest> actualResponse = productRestController.deleteById(productId);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).deleteById(productId);
    }

    @Test
    void testUpdateProduct_DirectCall() throws IOException {
        // Given
        Long productId = 1L;
        ResponseEntity<ProductResponseRest> expectedResponse = ResponseEntity.ok(testResponse);
        when(productService.update(any(Product.class), eq(2L), eq(productId))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ProductResponseRest> actualResponse = productRestController.update(
                productId, testImageFile, "Updated Product", 150, 5, 2L);

        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(productService, times(1)).update(any(Product.class), eq(2L), eq(productId));
    }

    @Test
    void testSaveProduct_ImageValidation_SizeExceeded() throws IOException {
        // Given
        MockMultipartFile largeImageFile = new MockMultipartFile(
                "picture",
                "large-image.jpg",
                "image/jpeg",
                new byte[11 * 1024 * 1024] // 11MB - exceeds 10MB limit
        );

        // When
        ResponseEntity<ProductResponseRest> response = productRestController.save(
                largeImageFile, "Test Product", 100, 10, 1L);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMetadata());
        assertFalse(response.getBody().getMetadata().isEmpty());
        assertEquals("Erro na validação", response.getBody().getMetadata().get(0).get("type"));
        verify(productService, never()).save(any(Product.class), anyLong());
    }

    @Test
    void testSaveProduct_ImageValidation_InvalidType() throws IOException {
        // Given
        MockMultipartFile invalidFile = new MockMultipartFile(
                "picture",
                "test.txt",
                "text/plain",
                "not an image".getBytes()
        );

        // When
        ResponseEntity<ProductResponseRest> response = productRestController.save(
                invalidFile, "Test Product", 100, 10, 1L);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getMetadata());
        assertFalse(response.getBody().getMetadata().isEmpty());
        assertEquals("Erro na validação", response.getBody().getMetadata().get(0).get("type"));
        verify(productService, never()).save(any(Product.class), anyLong());
    }
}
