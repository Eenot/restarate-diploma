package ru.yandex.practicum.restarate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.storage.impl.PricingDbStorageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PricingDbStorageImplTest {

    private final JdbcTemplate jdbcTemplate;
    private PricingDbStorageImpl pricingDbStorage;

    @BeforeEach
    void prepareData() {
        pricingDbStorage = new PricingDbStorageImpl(jdbcTemplate);
    }

    @Test
    public void findAll() {
        var expected = List.of(
                new Catalog(1L, "$"),
                new Catalog(2L, "$$"),
                new Catalog(3L, "$$$"),
                new Catalog(4L, "$$$$"),
                new Catalog(5L, "$$$$$")
        );
        var result = pricingDbStorage.getAll();

        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void findPricingById() {
        var expected = new Catalog(5L, "$$$$$");

        var result = pricingDbStorage.getById(5L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

}
