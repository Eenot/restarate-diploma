package ru.yandex.practicum.restarate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.service.CategoryService;
import ru.yandex.practicum.restarate.storage.impl.CategoryDbStorageImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDbStorageImpl genreStorage;

    @Override
    public List<Catalog> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Catalog getById(Long id) {
        return genreStorage.getById(id);
    }
}
