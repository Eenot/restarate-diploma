package ru.yandex.practicum.restarate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.storage.impl.CategoryDbStorageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryDbStorageImplTest {

    private final JdbcTemplate jdbcTemplate;
    private CategoryDbStorageImpl categoryDbStorage;

    @BeforeEach
    void prepareData() {
        categoryDbStorage = new CategoryDbStorageImpl(jdbcTemplate);
    }

    @Test
    public void findAllCategories() {
        var expected = List.of(
                new Catalog(1L, "Десерт"),
                new Catalog(2L, "Напиток"),
                new Catalog(3L, "Паста"),
                new Catalog(4L, "Роллы"),
                new Catalog(5L, "Супы"),
                new Catalog(6L, "Гарниры")
        );
        var result = categoryDbStorage.getAll();

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void findCategoryById() {
        var expected = new Catalog(3L, "Паста");

        var result = categoryDbStorage.getById(3L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);

    }


}
