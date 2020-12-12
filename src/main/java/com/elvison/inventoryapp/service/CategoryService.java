package com.elvison.inventoryapp.service;

import com.elvison.inventoryapp.exception.ResourceNotFoundException;
import com.elvison.inventoryapp.helper.DefaultNameValidationHelper;
import com.elvison.inventoryapp.model.Category;
import com.elvison.inventoryapp.model.rest.CategoryRequest;
import com.elvison.inventoryapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private DefaultNameValidationHelper validationHelper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void createCategory(CategoryRequest request) {
        validationHelper.validate(request);

        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category with the same name already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.save(category);
    }

    public List<Category> getCategories(String nameFilter) {
        if (StringUtils.isEmpty(nameFilter)) {
            return categoryRepository.findAll();
        }
        return categoryRepository.findByNameLike(nameFilter);
    }

    public Optional<Category> getCategory(Integer id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public void updateCategory(Integer id, CategoryRequest request) {
        validationHelper.validate(request);

        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category with the same name already exists");
        }

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(request.getName());
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
