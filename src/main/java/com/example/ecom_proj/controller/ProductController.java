package com.example.ecom_proj.controller;

import com.example.ecom_proj.model.Product;
import com.example.ecom_proj.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping("/")
    public String greet() {
        return "Hello, mate";
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")//but is better to specify
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("products/{id}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable int id){
        Product product = productService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestBody String keyword){
        List<Product> products = productService.searchProduct(keyword);
        System.out.println("Searching for products with : " + keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }




    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestPart("product") Product product,
                                        @RequestPart("imageFile") MultipartFile imageFile) {
        try {
            return new ResponseEntity<>(
                    productService.addProduct(product, imageFile),
                    HttpStatus.CREATED
            );
        } catch(Exception e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("/products/{id}/image")
    public ResponseEntity<?> updateProductImageById(@PathVariable int id, @RequestBody MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return new ResponseEntity<>("No image provided", HttpStatus.BAD_REQUEST);
        }

        try {
            Product updatedProduct = productService.updateProductImageById(id, imageFile);
            if (updatedProduct == null) {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/products")
    public void updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
    }


    @DeleteMapping("/products/{prodId}")
    public void deleteProduct(@PathVariable int prodId) {
        productService.deleteProductsById(prodId);
    }
}
