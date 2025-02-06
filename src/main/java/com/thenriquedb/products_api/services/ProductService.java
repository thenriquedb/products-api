package com.thenriquedb.products_api.services;

import com.thenriquedb.products_api.controllers.ProductController;
import com.thenriquedb.products_api.dtos.ProductRecordDto;
import com.thenriquedb.products_api.execptions.ProductNotFoundExecption;
import com.thenriquedb.products_api.domain.Product;
import com.thenriquedb.products_api.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAllProducts() {
        List<Product> products = this.productRepository.findAll();

        if(!products.isEmpty()) {
            for(Product product : products) {
                UUID id = product.getId();
                Link link = linkTo(methodOn(ProductController.class).getById(id)).withSelfRel();
                product.add(link);
            }
        }

        return products;
    }

    public Product getProductById(UUID id) throws ProductNotFoundExecption {
        Optional<Product> product = productRepository.findById(id);

        if(product.isEmpty()) {
            throw new ProductNotFoundExecption(id);
        }

        Link link = linkTo(methodOn(ProductController.class).listAllProducts()).withSelfRel();
        product.get().add(link);

        return product.get();
    }

    public Product createProduct(ProductRecordDto productRecordDto) {
        var productModel  = new Product();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return productRepository.save(productModel);
    }

    public Product updateProduct(UUID id, ProductRecordDto productRecordDto) throws ProductNotFoundExecption {
        Optional<Product> product = this.productRepository.findById(id);
        if(product.isEmpty()) {
            throw new ProductNotFoundExecption(id);
        }

        var productModel = product.get();
        BeanUtils.copyProperties(productRecordDto, productModel);

        return productRepository.save(productModel);
    }

    public void deleteProduct(UUID id) throws ProductNotFoundExecption {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) {
            throw new ProductNotFoundExecption(id);
        }

        productRepository.deleteById(id);
    }
}
