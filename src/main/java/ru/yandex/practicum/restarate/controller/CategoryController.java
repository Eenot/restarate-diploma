package ru.yandex.practicum.restarate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.service.impl.CategoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryServiceImpl categoryServiceImpl;

    public CategoryController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @GetMapping
    public List<Catalog> getAll() {
        return categoryServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public Catalog getById(@PathVariable Long id) {
        return categoryServiceImpl.getById(id);
    }

}
