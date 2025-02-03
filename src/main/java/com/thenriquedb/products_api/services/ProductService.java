package com.thenriquedb.products_api.services;

import com.thenriquedb.products_api.controllers.ProductController;
import com.thenriquedb.products_api.dtos.ProductRecordDto;
import com.thenriquedb.products_api.execptions.ProductNotFoundExecption;
import com.thenriquedb.products_api.models.ProductModel;
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

    public List<ProductModel> listAllProducts() {
        List<ProductModel> products = this.productRepository.findAll();

        if(!products.isEmpty()) {
            for(ProductModel product : products) {
                UUID id = product.getId();
                Link link = linkTo(methodOn(ProductController.class).getById(id)).withSelfRel();
                product.add(link);
            }
        }

        return products;
    }

    public ProductModel getProductById(UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);

        if(product.isEmpty()) {
            throw new ProductNotFoundExecption(id);
        }

        Link link = linkTo(methodOn(ProductController.class).listAllProducts()).withSelfRel();
        product.get().add(link);

        return product.get();
    }

    public ProductModel createProduct(ProductRecordDto productRecordDto) {
        var productModel  = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return productRepository.save(productModel);
    }

    public ProductModel updateProduct(UUID id, ProductRecordDto productRecordDto) {
        Optional<ProductModel> product = this.productRepository.findById(id);
        if(product.isEmpty()) {
            throw new ProductNotFoundExecption(id);
        }

        var productModel = product.get();
        BeanUtils.copyProperties(productRecordDto, productModel);

        return productRepository.save(productModel);
    }

    public void deleteProduct(UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);
        if(product.isEmpty()) {
            throw new ProductNotFoundExecption(id);
        }

        productRepository.deleteById(id);
    }
}
