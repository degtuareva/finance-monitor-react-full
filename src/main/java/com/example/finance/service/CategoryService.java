package com.example.finance.service;

import com.example.finance.dto.CategoryRequest;
import com.example.finance.entity.Category;
import com.example.finance.repository.CategoryRepository;
import com.example.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categories;
    private final UserRepository users;

    @Transactional(readOnly = true)
    public List<Category> list(Long u) {
        return categories.findByUserIdOrderByName(u);
    }

    public Category create(Long u, CategoryRequest r) {
        if (categories.existsByNameIgnoreCaseAndUserId(r.getName(), u))
            throw new IllegalArgumentException("Категория уже существует");
        Category c = new Category();
        c.setName(r.getName().trim());
        c.setType(r.getType());
        c.setUser(users.findById(u).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден")));
        return categories.save(c);
    }

    public void delete(Long u, Long id) {
        categories.delete(categories.findByIdAndUserId(id, u).orElseThrow(() -> new IllegalArgumentException("Категория не найдена")));
    }
}
