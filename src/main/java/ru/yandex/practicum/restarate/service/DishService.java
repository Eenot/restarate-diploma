package ru.yandex.practicum.restarate.service;

import ru.yandex.practicum.restarate.model.Dish;

import javax.validation.Valid;
import java.util.List;

public interface DishService {

    List<Dish> getAll();

    Dish create(@Valid Dish dish);

    Dish update(@Valid Dish dish);

    void remove(Long id);

    void likeDish(Long dishId, Long userId);

    void dislikeDish(Long dishId, Long userId);

    List<Dish> getPopularDishes(Long count, Long categoryId, Integer year);

    Dish getDishById(Long id);

    List<Dish> getAuthorDishes(long authorId, String sortBy);

    List<Dish> search(String query, String by);

    List<Dish> getPopularDishListOfUserAndFriend(Long userId, Long friendId);
}
