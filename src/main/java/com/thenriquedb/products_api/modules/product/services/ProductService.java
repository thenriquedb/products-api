package com.thenriquedb.products_api.modules.product.services;

import com.thenriquedb.products_api.infra.responses.PaginationResponse;
import com.thenriquedb.products_api.modules.product.controllers.ProductController;
import com.thenriquedb.products_api.modules.product.dtos.ProductRecordDto;
import com.thenriquedb.products_api.infra.execptions.ProductNotFoundExecption;
import com.thenriquedb.products_api.domain.Product;
import com.thenriquedb.products_api.repositories.ProductRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {
    public static final String REPORT_PATH = "classpath:jasper/templates/";
    public static final String REPORT_NAME = "ProductsReport.jrxml";
    public static final String REPORT_OUTPUT = "temp/jasper/outputs/";

    @Autowired
    private ProductRepository productRepository;

    public PaginationResponse<Product> listAllProducts(int pageNumber, int pageSize, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Product> products = this.productRepository.findAll(
                PageRequest.of(pageNumber, pageSize, sort)
        );

        if(!products.isEmpty()) {
            for(Product product : products.getContent()) {
                UUID id = product.getId();
                Link link = linkTo(methodOn(ProductController.class).getById(id)).withSelfRel();
                product.add(link);
            }
        }

        return new PaginationResponse<Product>(
                products.getContent(),
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages()
        );
    }

    public Product getProductById(UUID id) throws ProductNotFoundExecption {
        Optional<Product> product = productRepository.findById(id);

        if(product.isEmpty()) {
            throw new ProductNotFoundExecption(id);
        }

//        Link link = linkTo(methodOn(ProductController.class).listAllProducts(0,10,"createdAt", true).withSelfRel();
//        product.get().add(link);

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

    public String generateReport() {
        List<Product> products = this.productRepository.findAll();

        try {
            String reportPath = ResourceUtils.getFile(REPORT_PATH + REPORT_NAME).getAbsolutePath();

            var datasource = new JRBeanCollectionDataSource(products);
            JasperReport jasper = JasperCompileManager.compileReport(reportPath);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, null, datasource);

            String outputPath = ResourceUtils.getFile(REPORT_OUTPUT).getAbsolutePath();
            String filename = "produtos_" + System.currentTimeMillis();
            String  fullPath = outputPath + "/" + filename + ".pdf";

            JasperExportManager.exportReportToPdfFile(jasperPrint, fullPath);

            return fullPath;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
