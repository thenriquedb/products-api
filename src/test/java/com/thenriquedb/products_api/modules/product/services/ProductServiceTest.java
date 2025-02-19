package com.thenriquedb.products_api.modules.product.services;

import com.thenriquedb.products_api.domain.Product;
import com.thenriquedb.products_api.infra.execptions.ProductNotFoundExecption;
import com.thenriquedb.products_api.modules.product.dtos.ProductRecordDto;
import com.thenriquedb.products_api.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listAllProducts_success() {
        ProductRecordDto productRecordDto1 = new ProductRecordDto("Product 1", new BigDecimal("10.00"));
        ProductRecordDto productRecordDto2 = new ProductRecordDto("Product 2", new BigDecimal("20.00"));
        ProductRecordDto productRecordDto3 = new ProductRecordDto("Product 3", new BigDecimal("20.00"));

        Product product1 = new Product();
        BeanUtils.copyProperties(productRecordDto1, product1);
        product1.setId(UUID.randomUUID());

        Product product2 = new Product();
        BeanUtils.copyProperties(productRecordDto2, product2);
        product2.setId(UUID.randomUUID());

        Product product3 = new Product();
        BeanUtils.copyProperties(productRecordDto3, product3);
        product3.setId(UUID.randomUUID());

        when(productRepository.save(product1)).thenReturn(product1);
        when(productRepository.save(product2)).thenReturn(product2);
        when(productRepository.save(product3)).thenReturn(product3);
        when(productRepository.findAll()).thenReturn(List.of(product1, product2, product3));

        List<Product> products = productService.listAllProducts();
        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    void listAllProducts_empty() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> products = productService.listAllProducts();
        assertThat(products.size()).isEqualTo(0);
    }

    @Test
    void getProductById_success() {
        ProductRecordDto productRecordDto = new ProductRecordDto("Product 1", new BigDecimal("10.00"));

        Product product = new Product();
        BeanUtils.copyProperties(productRecordDto, product);
        product.setId(UUID.randomUUID());

        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Product createdProduct = productService.createProduct(productRecordDto);
        Product findedProduct = productService.getProductById(createdProduct.getId());

        assertThat(findedProduct.getId()).isEqualTo(createdProduct.getId());
        verify(productRepository).findById(createdProduct.getId());
    }

    @Test
    void getProductById_throwsProductNotFoundExecption() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundExecption.class, () -> productService.getProductById(id));
        verify(productRepository).findById(id);
    }
    @Test
    void createProduct_success() {
        ProductRecordDto productRecordDto = new ProductRecordDto("Product 1", new BigDecimal("10.00"));

        Product product = new Product();
        BeanUtils.copyProperties(productRecordDto, product);
        product.setId(UUID.randomUUID());

        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Product createdProduct = productService.createProduct(productRecordDto);
        Product findedProduct = productRepository.findById(createdProduct.getId()).get();

        assertThat(findedProduct.getId()).isEqualTo(createdProduct.getId());
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_success() {
        ProductRecordDto productRecordDto = new ProductRecordDto("Product 1", new BigDecimal("10.00"));

        Product product = new Product();
        BeanUtils.copyProperties(productRecordDto, product);
        product.setId(UUID.randomUUID());

        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Product createdProduct = productService.createProduct(productRecordDto);

        ProductRecordDto updatedProductRecordDto = new ProductRecordDto("Product 2", new BigDecimal("20.00"));
        Product updatedProduct = productService.updateProduct(createdProduct.getId(), updatedProductRecordDto);

        assertThat(createdProduct.getId()).isEqualTo(updatedProduct.getId());
        assertThat(updatedProduct.getName()).isEqualTo(updatedProductRecordDto.name());
        assertThat(updatedProduct.getPrice()).isEqualTo(updatedProductRecordDto.price());
    }

    @Test
    void updateProduct_throwsProductNotFoundExeception() {
        UUID id = UUID.randomUUID();
        ProductRecordDto productRecordDto = new ProductRecordDto("Product 1", new BigDecimal("10.00"));

        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundExecption.class, () -> productService.updateProduct(id, productRecordDto));
    }

    @Test
    void deleteProduct_success() {
        ProductRecordDto productRecordDto = new ProductRecordDto("Product 1", new BigDecimal("10.00"));

        Product product = new Product();
        BeanUtils.copyProperties(productRecordDto, product);
        product.setId(UUID.randomUUID());

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(product.getId());

        assertDoesNotThrow(() -> productService.deleteProduct(product.getId()));
        verify(productRepository).deleteById(product.getId());
    }

    @Test
    void deleteProduct_throwsProductNotFoundExecption() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundExecption.class, () -> productService.deleteProduct(id));
    }

}