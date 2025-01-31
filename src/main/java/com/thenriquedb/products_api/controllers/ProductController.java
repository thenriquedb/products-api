package com.thenriquedb.products_api.controllers;

import com.thenriquedb.products_api.dtos.ProductRecordDto;
import com.thenriquedb.products_api.models.ProductModel;
import com.thenriquedb.products_api.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        ProductModel createdProduct = this.productService.createProduct(productRecordDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> listAllProducts() {
        List<ProductModel> products = this.productService.listAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") UUID id) {
        ProductModel product = this.productService.getProductById(id);

        if(product == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                     .body("Product not found");
        }


        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        try
            var product = this.productService.updateProduct(id, productRecordDto);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable("id") UUID id) {
        try {
            this.productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }
    }
}
