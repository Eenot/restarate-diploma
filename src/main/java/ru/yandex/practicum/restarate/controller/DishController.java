package ru.yandex.practicum.restarate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.restarate.model.Dish;
import ru.yandex.practicum.restarate.service.impl.DishServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping(value = "/dishes")
@Slf4j
public class DishController {

    private final DishServiceImpl dishServiceImpl;

    public DishController(DishServiceImpl dishServiceImpl) {
        this.dishServiceImpl = dishServiceImpl;
    }

    @GetMapping
    public List<Dish> getAllDishes() {
        return dishServiceImpl.getAll();
    }

    @PostMapping
    public Dish createDish(@Valid @RequestBody Dish body) {
        return dishServiceImpl.create(body);
    }

    @PutMapping
    public Dish updateDish(@Valid @RequestBody Dish body) {
        return dishServiceImpl.update(body);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeDish(@PathVariable Long id, @PathVariable Long userId) {
        dishServiceImpl.likeDish(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislikeDish(@PathVariable Long id, @PathVariable Long userId) {
        dishServiceImpl.dislikeDish(id, userId);
    }

    @GetMapping("/popular")
    public List<Dish> getPopularDishList(@RequestParam(defaultValue = "10", required = false) Long count,
                                         @RequestParam(required = false) Long categoryId,
                                         @RequestParam(required = false) Integer year) {
        return dishServiceImpl.getPopularDishes(count, categoryId, year);
    }

    @GetMapping("/{id}")
    public Dish getDishById(@PathVariable Long id) {
        return dishServiceImpl.getDishById(id);
    }

    @DeleteMapping("/{dishId}")
    public void removeDishById(@PathVariable Long dishId) {
        dishServiceImpl.remove(dishId);
    }

    @GetMapping("/author/{authorId}")
    public List<Dish> getAuthorDishes(@PathVariable(name = "authorId") Long authorId,
                                      @RequestParam String sortBy) {
        return dishServiceImpl.getAuthorDishes(authorId, sortBy);
    }

    @GetMapping("/search")
    public List<Dish> findDishes(@NotEmpty @RequestParam String query,
                                 @NotEmpty @RequestParam String by) {
        return dishServiceImpl.search(query, by);
    }

    @GetMapping("/common")
    public List<Dish> getPopularDishListOfUserAndFriend(@RequestParam(name = "userId") Long userId, @RequestParam(name = "friendId") Long friendId) {
        return dishServiceImpl.getPopularDishListOfUserAndFriend(userId, friendId);
    }
}
