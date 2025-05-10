package ru.yandex.practicum.restarate.storage;

import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.model.Dish;

import java.util.List;
import java.util.Set;

public interface CreatorStorage {
    List<Catalog> getAll();

    Catalog getById(Long id);

    Catalog save(Catalog catalog);

    Catalog update(Catalog catalog);

    void deleteById(Long id);

    void addDishAuthors(Long dishId, Set<Catalog> authors);

    void updateDishAuthors(Long dishId, Set<Catalog> authors);

    List<Dish> getAuthorDishes(Long authorsId, String sortBy);

    List<Catalog> getDishAuthors(Long dishId);

}
