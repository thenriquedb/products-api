package com.thenriquedb.products_api.modules.product.controllers;

import com.thenriquedb.products_api.configurations.SecurityConfiguration;
import com.thenriquedb.products_api.infra.responses.PaginationResponse;
import com.thenriquedb.products_api.modules.product.dtos.ProductRecordDto;
import com.thenriquedb.products_api.domain.Product;
import com.thenriquedb.products_api.modules.product.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Tag(name ="Products", description = "Products CRUD")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created")
    public ResponseEntity<Product> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        Product createdProduct = this.productService.createProduct(productRecordDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    @GetMapping
    @Operation(summary = "List all products")
    public ResponseEntity<PaginationResponse<Product>> listAllProducts(@RequestParam(value = "page", defaultValue = "0") int pageNumber,
                                                         @RequestParam(value = "size", defaultValue = "10") int pageSize,
                                                         @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                         @RequestParam(value = "ascending", defaultValue = "true") boolean ascending) {
        PaginationResponse<Product> response = this.productService.listAllProducts(pageNumber, pageSize, sortBy, ascending);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Object> getById(@PathVariable("id") UUID id) {
        Product product = this.productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product by id")
    @ApiResponse(responseCode = "200", description = "Product updated")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        var product = this.productService.updateProduct(id, productRecordDto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by id")
    @ApiResponse(responseCode = "200", description = "Product updated")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Object> deleteById(@PathVariable("id") UUID id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.ok(String.format("Product with id %s deleted", id));
    }

    @GetMapping("/report")
    public ResponseEntity<Object> generateReport() throws IOException {
        String filePath = this.productService.generateReport();

        File file = new File(filePath);
        Path path = Paths.get(file.getAbsolutePath());
        var resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(file.length())
                .body(resource);    }
}
