package com.elvison.inventoryapp.service;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.helper.ProductRequestValidationHelper;
import com.elvison.inventoryapp.model.Category;
import com.elvison.inventoryapp.model.Product;
import com.elvison.inventoryapp.model.rest.ProductRequest;
import com.elvison.inventoryapp.repository.CategoryRepository;
import com.elvison.inventoryapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRequestValidationHelper validationHelper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void createProduct(ProductRequest request) {
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Given category does not exist"));

        validationHelper.validate(request, category);

        if (productRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Product with the same name already exists");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setCategoryId(request.getCategoryId());
        productRepository.save(product);
    }

    public List<Product> getProducts(String nameFilter) {
        if (StringUtils.isEmpty(nameFilter)) {
            return productRepository.findAll();
        }
        return productRepository.findByNameLike(nameFilter);
    }

    public Optional<Product> getProduct(Integer id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void updateProduct(Integer id, ProductRequest request) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getName().equals(request.getName())) {
            if (productRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Product with the same name already exists");
            }
            product.setName(request.getName());
        }

        Category category;
        if (!request.getCategoryId().equals(product.getCategoryId())) {
            category = categoryRepository
                    .findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Given category does not exist"));
            product.setCategoryId(category.getId());
        } else {
            category = product.getCategory();
        }

        validationHelper.validate(request, category);
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

}
