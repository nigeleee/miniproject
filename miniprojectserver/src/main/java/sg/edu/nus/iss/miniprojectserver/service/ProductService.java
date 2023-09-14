package sg.edu.nus.iss.miniprojectserver.service;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.miniprojectserver.entity.Product;
import sg.edu.nus.iss.miniprojectserver.repository.ProductRepo;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(Long id) {
        return productRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    public void updateInventory(Long productId, int quantityChange) {
        Product product = getProductById(productId);
        if (product != null) {
            int newQuantity = product.getQuantity() + quantityChange;
            product.setQuantity(newQuantity);
            productRepo.save(product);
        }
    }
}


