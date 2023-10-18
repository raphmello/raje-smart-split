package com.raje.smartsplit.service;

import com.raje.smartsplit.entity.BillCategory;
import com.raje.smartsplit.exception.CategoryNotFoundException;
import com.raje.smartsplit.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public BillCategory findById(Long categoryId) {
        Optional<BillCategory> optional = repository.findById(categoryId);
        if (optional.isEmpty()) {
            throw new CategoryNotFoundException();
        }
        return optional.get();
    }

    public Set<BillCategory> findAll() {
        return new HashSet<>(repository.findAll());
    }

    public Set<BillCategory> findAllByGroupId(Long groupId) {
        return repository.findAllByGroupId(groupId);
    }
}
