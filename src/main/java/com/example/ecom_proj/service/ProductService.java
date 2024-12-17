package com.example.ecom_proj.service;

import com.example.ecom_proj.model.Product;
import com.example.ecom_proj.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepo repo;

    public ProductService() {
    }

    public List<Product> getAllProducts() {
        List<Product> products = repo.findAll();
        for (int i = 0; i < products.size(); i++) {


        System.out.println("Retrieved products: " + products.get(i));}
        return products;
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile productImage) throws IOException {
        product.setImageData(productImage.getBytes());
        product.setImageName(productImage.getOriginalFilename());
        product.setImageType(productImage.getContentType());
        return repo.save(product);
    }

//    public Product addProduct(Product product) {
//        return repo.save(product);
//    }

    public void updateProduct(Product product1) {
        repo.save(product1);
    }

    public void deleteProductsById(int prodId) {
        repo.deleteById(prodId);
    }

    public Product updateProductImageById(int id, MultipartFile imageFile) {
        Product product = repo.findById(id).orElse(null);
        if (product == null) {
            return null;
        }

        try {
            product.setImageData(imageFile.getBytes());
            product.setImageType(imageFile.getContentType());
            product.setImageName(imageFile.getOriginalFilename());
        } catch (Exception e) {
            System.out.println("Error processing image: " + e.getMessage());
            return null;
        }

        repo.save(product);
        return product;
    }

    public List<Product> searchProduct(String keyword) {
        return repo.searchProduct(keyword);
    }
}
