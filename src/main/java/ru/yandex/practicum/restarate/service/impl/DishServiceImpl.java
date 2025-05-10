package ru.yandex.practicum.restarate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.restarate.error.ValidationException;
import ru.yandex.practicum.restarate.model.Dish;
import ru.yandex.practicum.restarate.model.Event;
import ru.yandex.practicum.restarate.model.EventOperation;
import ru.yandex.practicum.restarate.model.EventType;
import ru.yandex.practicum.restarate.service.DishService;
import ru.yandex.practicum.restarate.storage.CreatorStorage;
import ru.yandex.practicum.restarate.storage.DishStorage;
import ru.yandex.practicum.restarate.storage.UserStorage;
import ru.yandex.practicum.restarate.storage.impl.EventDbStorage;
import ru.yandex.practicum.restarate.utils.DishIdGenerator;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.restarate.utils.DefaultData.ENTITY_PROCESSED_SUCCESSFUL;
import static ru.yandex.practicum.restarate.utils.DefaultData.DISH_RELEASE_DATE;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishStorage dishStorage;
    private final UserStorage userStorage;
    private final CreatorStorage creatorStorage;
    private final EventDbStorage eventStorage;

    @Override
    public List<Dish> getAll() {
        return dishStorage.getAll();
    }

    @Override
    public Dish create(Dish dish) {
        validateDish(dish);
        dishStorage.save(dish);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, dish);
        return dish;
    }

    @Override
    public Dish update(Dish dish) {
        validateDish(dish);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, dish);
        return dishStorage.update(dish);
    }

    @Override
    public void remove(Long id) {
        dishStorage.removeDishById(id);
    }

    @Override
    public void likeDish(Long dishId, Long userId) {
        userStorage.getUserById(userId);
        dishStorage.getDishById(dishId);
        dishStorage.likeDish(userId, dishId);
        eventStorage.addEvent(new Event(userId, EventType.LIKE, EventOperation.ADD, dishId));
    }

    @Override
    public void dislikeDish(Long dishId, Long userId) {
        userStorage.getUserById(userId);
        dishStorage.getDishById(dishId);
        dishStorage.dislikeDish(userId, dishId);
        eventStorage.addEvent(new Event(userId, EventType.LIKE, EventOperation.REMOVE, dishId));
    }

    @Override
    public List<Dish> getPopularDishes(Long count, Long categoryId, Integer year) {
        return dishStorage.getPopularDishes(count, categoryId, year);
    }

    @Override
    public Dish getDishById(Long id) {
        return dishStorage.getDishById(id);
    }

    @Override
    public List<Dish> getAuthorDishes(long authorId, String sortBy) {
        return creatorStorage.getAuthorDishes(authorId, sortBy);
    }

    @Override
    public List<Dish> search(String query, String by) {
        return dishStorage.search(query, by);
    }

    @Override
    public List<Dish> getPopularDishListOfUserAndFriend(Long userId, Long friendId) {
        List<Dish> first = dishStorage.getDishesByUser(userId);
        List<Dish> second = dishStorage.getDishesByUser(friendId);
        first.retainAll(second);
        return first;
    }

    private void validateDish(@Valid Dish dish) {
        if (dish.getReleaseDate().isBefore(DISH_RELEASE_DATE)) {
            ValidationException e = new ValidationException("Дата релиза (releaseDate) не может быть раньше 01.01.2001");
            log.warn(e.getMessage());
            throw e;
        }
        if (dish.getId() == null) {
            dish.setId(DishIdGenerator.getInstance().getId());
        }
    }
}
