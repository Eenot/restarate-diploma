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
import ru.yandex.practicum.restarate.storage.impl.CreatorDbStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private CreatorDbStorage authorDbStorage;

    @BeforeEach
    void prepareData() {
        authorDbStorage = new CreatorDbStorage(jdbcTemplate);
    }

    @Test
    public void testShouldReturnGetAllAuthors() {
        var expected = List.of(getDefaultAuthor());

        var result = authorDbStorage.getAll();
        assertThat(result)
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void testShouldReturnCreateAuthor() {
        var expected = getDefaultAuthor();
        var result = authorDbStorage.getById(expected.getId());

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void testShouldReturnUpdateAuthor() {
        var first = getDefaultAuthor();
        var updated = getDefaultAuthor2();
        updated.setId(first.getId());

        authorDbStorage.update(updated);

        var result = authorDbStorage.getById(updated.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    public void testShouldReturnGetByIdAuthor() {
        var expected = getDefaultAuthor();
        var result = authorDbStorage.getById(expected.getId());

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void testShouldDeleteAuthor() {
        var authors = getDefaultAuthor();
        authorDbStorage.deleteById(authors.getId());
        var result = authorDbStorage.getAll();

        assertThat(result).isEmpty();
    }

    private Catalog getDefaultAuthor() {
        Catalog catalog = new Catalog(1L, "author");
        Catalog savedAuthor = authorDbStorage.save(catalog);
        return authorDbStorage.getById(savedAuthor.getId());
    }

    private Catalog getDefaultAuthor2() {
        Catalog catalog = new Catalog(1L, "newAuthor");
        Catalog savedAuthor = authorDbStorage.save(catalog);
        return authorDbStorage.getById(savedAuthor.getId());
    }
}
