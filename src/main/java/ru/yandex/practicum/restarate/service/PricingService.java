package ru.yandex.practicum.restarate.service;

import ru.yandex.practicum.restarate.model.Catalog;

import java.util.List;

public interface PricingService {
    List<Catalog> getAll();

    Catalog getById(Long id);
}
