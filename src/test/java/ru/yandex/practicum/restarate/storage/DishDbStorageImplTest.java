package ru.yandex.practicum.restarate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.model.Dish;
import ru.yandex.practicum.restarate.model.User;
import ru.yandex.practicum.restarate.storage.impl.CreatorDbStorage;
import ru.yandex.practicum.restarate.storage.impl.DishDbStorageImpl;
import ru.yandex.practicum.restarate.storage.impl.UserDbStorageImpl;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DishDbStorageImplTest {

    private final JdbcTemplate jdbcTemplate;
    private DishDbStorageImpl storage;
    private CreatorDbStorage authorDbStorage;

    @BeforeEach
    void prepareData() {
        storage = new DishDbStorageImpl(jdbcTemplate);
        authorDbStorage = new CreatorDbStorage(jdbcTemplate);
    }

    @Test
    public void getAll() {
        var expected = List.of(getDefaultDish(), getSecondDish());
        expected.forEach(storage::save);

        var result = storage.getAll();
        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void create() {
        var expected = getDefaultDish();

        storage.save(expected);
        var result = storage.getDishById(expected.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void update() {
        var first = getDefaultDish();
        var updated = getSecondDish();
        updated.setId(first.getId());
        storage.save(first);

        storage.update(updated);

        var result = storage.getDishById(updated.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    public void remove() {
        var expected = List.of(getDefaultDish());
        var second = getSecondDish();
        expected.forEach(storage::save);
        storage.save(second);

        storage.removeDishById(second.getId());

        var result = storage.getAll();

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void getById() {
        var expected = getDefaultDish();
        storage.save(expected);

        var result = storage.getDishById(expected.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void getPopularDishes() {
        UserDbStorageImpl userDbStorage = new UserDbStorageImpl(jdbcTemplate);

        Dish savedDish = storage.save(getDefaultDish());
        Dish secondSavedDish = storage.save(getSecondDish());
        userDbStorage.save(new User(1L, "first@email.ru", "firstLogin", "firstName", LocalDate.parse("2001-01-01")));
        storage.likeDish(1L, 1L);

        var expected = List.of(savedDish, secondSavedDish);
        var result = storage.getPopularDishes(10L, null, null);

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void getPopularDishListOfUserAndFriend() {
        UserDbStorageImpl userDbStorage = new UserDbStorageImpl(jdbcTemplate);

        userDbStorage.save(getDefaultUser());
        userDbStorage.save(getDefaultSecondUser());
        Dish savedDish = storage.save(getDefaultDish());
        Dish secondSavedDish = storage.save(getSecondDish());
        storage.likeDish(1L, 1L);
        storage.likeDish(2L, 2L);
        storage.likeDish(1L, 2L);
        userDbStorage.addFriendship(1L, 2L);

        var expected = List.of(savedDish, secondSavedDish);
        var result = storage.getDishesByUser(1L);

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);

    }

    private User getDefaultUser() {
        return new User(1L, "first@email.ru", "firstLogin", "firstName", LocalDate.parse("2000-01-01"));
    }

    private User getDefaultSecondUser() {
        return new User(2L, "second@email.ru", "secondLogin", "secondName", LocalDate.parse("2005-09-14"));
    }

    @Test
    void getRecommendations() {

        UserDbStorageImpl userStorage = new UserDbStorageImpl(jdbcTemplate);

        userStorage.save(new User(1L, "mail@ya.ru",
                "user01", "dude", LocalDate.of(1991, 1, 1)));

        userStorage.save(new User(2L, "another@mail.ru",
                "user02", "mikey", LocalDate.of(1991, 1, 1)));

        storage.save(getDefaultDish());
        storage.save(getSecondDish());

        storage.likeDish(1L, 1L);
        storage.likeDish(2L, 1L);
        storage.likeDish(1L, 2L);

        List<Dish> dishes = storage.getRecommendations(2L);

        assertThat(dishes)
                .isNotNull()
                .hasSize(dishes.size())
                .usingRecursiveComparison()
                .isEqualTo(List.of(getSecondDish()));
    }

    @Test
    void emptySearch() {
        storage.save(getDefaultDish());
        storage.save(getSecondDish());
        var result = storage.search("test", "author");

        assertThat(result).isEmpty();
    }

    @Test
    void searchByTitle() {
        storage.save(getDefaultDish());
        var expected = List.of(storage.save(getSecondDish()));

        var result = storage.search("second", "title");

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void searchByAuthor() {
        Dish first = storage.save(getDefaultDish());
        Dish second = storage.save(getSecondDish());
        var expected = List.of(first, second);

        var result = storage.search("AuT", "author");

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void searchByAuthorAndTitle() {
        Dish first = storage.save(getDefaultDish());
        storage.save(getSecondDish());
        var expected = List.of(first);

        var result = storage.search("fiRst", "title,author");

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private Dish getDefaultDish() {
        return new Dish(1L, "firstDish", "firstDescription", LocalDate.parse("2001-12-12"),
                122L, getDefaultPricing(), List.of(getDefaultCategory()), List.of(getDefaultAuthor()));
    }

    private Dish getSecondDish() {
        return new Dish(2L, "secondDish", "secondDescription", LocalDate.parse("2010-01-01"),
                99L, new Catalog(1L, "$"), List.of(new Catalog(3L, "Паста")),
                List.of(new Catalog(1L, "author")));
    }

    private Catalog getDefaultPricing() {
        return new Catalog(3L, "$$$");
    }

    private Catalog getDefaultCategory() {
        return new Catalog(2L, "Напиток");
    }

    private Catalog getDefaultAuthor() {
        Catalog catalog = new Catalog(1L, "author");
        Catalog savedAuthor = authorDbStorage.save(catalog);
        return authorDbStorage.getById(savedAuthor.getId());
    }

}
