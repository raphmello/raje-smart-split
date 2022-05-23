package com.raje.smartsplit.service;

import com.raje.smartsplit.entity.BillCategory;
import com.raje.smartsplit.exception.CategoryNotFoundException;
import com.raje.smartsplit.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public BillCategory findById(Long categoryId) {
        Optional<BillCategory> optional = repository.findById(categoryId);
        if (optional.isEmpty())
            throw new CategoryNotFoundException();
        return optional.get();
    }

    public List<BillCategory> findAll() {
        return repository.findAll();
    }
}
