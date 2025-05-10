package ru.yandex.practicum.restarate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.restarate.error.ValidationException;
import ru.yandex.practicum.restarate.model.Dish;
import ru.yandex.practicum.restarate.service.impl.DishServiceImpl;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.restarate.utils.DefaultData.DISH_RELEASE_DATE;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class DishServiceImplTest {

    @Autowired
    DishServiceImpl dishServiceImpl;

    @Test
    void validateFilmPositive() {
        Dish dish = getDefaultDish();

        assertDoesNotThrow(() -> dishServiceImpl.create(dish));
    }

    @Test
    void validateDishNullName() {
        Dish dish = getDefaultDish();
        dish.setName(null);

        String expectedMsg = "Необходимо указать имя";

        var result = assertThrows(ConstraintViolationException.class, () -> dishServiceImpl.create(dish));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateDishEmptyName() {
        Dish dish = getDefaultDish();
        dish.setName("");

        String expectedMsg = "Необходимо указать имя";

        var result = assertThrows(ConstraintViolationException.class, () -> dishServiceImpl.create(dish));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateDishDescription() {
        Dish dish = getDefaultDish();
        dish.setDescription("CegjxX2tfX776lj3f2NY6Wll5KGRlTbPYecErJeCxlDx9NErGgKyhJ2DwtFRJKOBMFdRaGPaOiWKK0VMd9SD3WXmjx0gOHQtPwoN8jYgOw60V8tLiXMeoJ6ea1QXAdHwXLhlwldAPB9lHPraQoQlZqoQfrycZiGBBFNSyv18WuvayZZlWy75AF02pZBDmSXYhlmUvlZK1");

        String expectedMsg = "Длинна описания (description) не может превышать 200 символов!";

        var result = assertThrows(ConstraintViolationException.class, () -> dishServiceImpl.create(dish));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateDishReleaseDate() {
        Dish dish = getDefaultDish();
        dish.setReleaseDate(DISH_RELEASE_DATE.minusDays(1));

        String expectedMsg = "Дата релиза (releaseDate) не может быть раньше 01.01.2001";

        var result = assertThrows(ValidationException.class, () -> dishServiceImpl.create(dish));
        assertEquals(expectedMsg, result.getMessage());
    }

    @Test
    void validateDishWeight() {
        Dish dish = getDefaultDish();
        dish.setWeight(0L);

        String expectedMsg = "Вес блюда(weight) должен быть положительным";

        var result = assertThrows(ConstraintViolationException.class, () -> dishServiceImpl.create(dish));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    private Dish getDefaultDish() {
        return new Dish(1L, "name", "CegjxX2tfX776lj3f2NY6Wll5KGRlTbPYecErJeCxlDx9NErGgKyhJ2DwtFRJKOBMFdRaGPaOiWKK0VMd9SD3WXmjx0gOHQtPwoN8jYgOw60V8tLiXMeoJ6ea1QXAdHwXLhlwldAPB9lHPraQoQlZqoQfrycZiGBBFNSyv18WuvayZZlWy75AF02pZBDmSXYhlmUvlZK", DISH_RELEASE_DATE, 100L, null, null, null);
    }

    private String getErrorMessage(String fullMsg) {
        return fullMsg.split(":")[1].substring(1);
    }

}