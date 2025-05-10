package ru.yandex.practicum.restarate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.service.AuthorService;
import ru.yandex.practicum.restarate.storage.CreatorStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final CreatorStorage creatorStorage;

    @Override
    public List<Catalog> getAll() {
        return creatorStorage.getAll();
    }

    @Override
    public Catalog getById(Long id) {
        return creatorStorage.getById(id);
    }

    @Override
    public Catalog createAuthor(Catalog catalog) {
        return creatorStorage.save(catalog);
    }

    @Override
    public Catalog updateAuthor(Catalog catalog) {
        return creatorStorage.update(catalog);
    }

    @Override
    public void deleteById(Long id) {
        creatorStorage.deleteById(id);
    }
}
