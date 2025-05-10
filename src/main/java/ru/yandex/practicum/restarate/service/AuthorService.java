package ru.yandex.practicum.restarate.service;

import ru.yandex.practicum.restarate.model.Catalog;

import java.util.List;

public interface AuthorService {
    List<Catalog> getAll();

    Catalog getById(Long id);

    Catalog createAuthor(Catalog catalog);

    Catalog updateAuthor(Catalog catalog);

    void deleteById(Long id);
}
