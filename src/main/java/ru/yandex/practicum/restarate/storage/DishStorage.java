package ru.yandex.practicum.restarate.storage;

import ru.yandex.practicum.restarate.model.Dish;

import java.util.List;

public interface DishStorage {
    List<Dish> getAll();

    Dish save(Dish dish);

    Dish update(Dish dish);

    void removeDishById(Long id);

    Dish getDishById(Long id);

    List<Dish> getPopularDishes(Long count, Long categoryId, Integer year);

    List<Dish> getRecommendations(Long userId);

    List<Dish> search(String query, String by);

    List<Dish> getDishesByUser(Long userId);

    void likeDish(Long filmId, Long userId);

    void dislikeDish(Long filmId, Long userId);


}
