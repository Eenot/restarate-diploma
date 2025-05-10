package ru.yandex.practicum.restarate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.restarate.error.NotExistException;
import ru.yandex.practicum.restarate.error.ValidationException;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.model.Dish;
import ru.yandex.practicum.restarate.storage.CreatorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreatorDbStorage implements CreatorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Catalog> getAll() {
        String sqlQuery = "SELECT * FROM authors";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Catalog getById(Long id) {
        try {
            String sqlQuery = "SELECT * FROM authors WHERE id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, (rs, row) -> mapRow(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException(id.toString());
        }
    }

    @Override
    public Catalog save(Catalog catalog) {
        jdbcTemplate.update("INSERT INTO authors (name) VALUES (?)",
                catalog.getName());
        return jdbcTemplate.queryForObject(
                "SELECT id, name FROM authors WHERE id = " +
                        "(SELECT MAX(id) FROM authors)",
                (rs, row) -> mapRow(rs));
    }

    @Override
    public Catalog update(Catalog catalog) {
        String sqlQuery = "UPDATE authors SET name = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                catalog.getName(),
                catalog.getId());
        return getById(catalog.getId());
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        String sqlQuery = "DELETE FROM authors WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addDishAuthors(Long dishId, Set<Catalog> authors) {
        StringBuilder sb = new StringBuilder("Запрос на добавление в блюдо с id = ")
                .append(dishId)
                .append(" авторов с id = ");
        List<Object[]> batch = new ArrayList<>();
        for (Catalog director : authors) {
            Object[] values = new Object[]{
                    dishId, director.getId()};
            sb.append(director.getId()).append(", ");
            batch.add(values);
        }
        jdbcTemplate.batchUpdate("INSERT INTO dish_authors (dish_id, author_id) VALUES (?, ?)", batch);
    }

    @Override
    public void updateDishAuthors(Long dishId, Set<Catalog> authors) {
        jdbcTemplate.update("DELETE FROM dish_authors WHERE dish_id = ?", dishId);
        addDishAuthors(dishId, authors);
    }


    @Override
    public List<Catalog> getDishAuthors(Long dishId) {
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM authors WHERE id IN " +
                "(SELECT author_id FROM dish_authors WHERE dish_id = ?)", (rs, row) -> mapRow(rs), dishId));
    }

    @Override
    public List<Dish> getAuthorDishes(Long authorsId, String sortBy) {
        getById(authorsId);
        if (sortBy.equals("year")) {
            return jdbcTemplate.query("SELECT f.*, " +
                            "p.name as pricing_name " +
                            "FROM dish as d LEFT OUTER JOIN pricing as p ON d.pricing = p.id  " +
                            "WHERE d.id IN " +
                            "(SELECT DISTINCT dish_id FROM dish_authors WHERE author_id = ?) ORDER BY release_date",
                    (rs, rowNum) -> makeDish(rs),
                    authorsId);
        } else if (sortBy.equals("likes")) {
            return jdbcTemplate.query("SELECT f.*, " +
                            "p.name as pricing_name " +
                            "FROM dish as d " +
                            "LEFT OUTER JOIN pricing as p ON d.pricing = p.id  " +
                            "LEFT JOIN (SELECT COUNT(user_id) AS likes_amount, dish_id as likes_dish_id " +
                            "FROM favorite_dishes GROUP BY dish_id) AS fd " +
                            "ON d.id = fd.likes_dish_id " +
                            "WHERE d.id IN " +
                            "(SELECT DISTINCT dish_id FROM dish_authors WHERE author_id = ?) " +
                            "ORDER BY likes_amount",
                    (rs, rowNum) -> makeDish(rs),
                    authorsId);
        }
        throw new ValidationException("Неизвестный тип сортировки");
    }

    private List<Catalog> getFilmGenres(Long dishId) {
        String sql = "SELECT * FROM category c " +
                "WHERE id IN (SELECT category_id FROM dish_category WHERE dish_id = ?);";
        return jdbcTemplate.query(sql, (rs, row) -> mapRow(rs), dishId);
    }

    private Catalog mapRow(ResultSet rs) throws SQLException {
        return new Catalog(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

    private Dish makeDish(ResultSet rs) throws SQLException {
        return new Dish(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getLong("weight"),
                new Catalog(rs.getLong("pricing"), rs.getString("pricing_name")),
                getFilmGenres(rs.getLong("id")),
                getDishAuthors(rs.getLong("id"))
        );
    }

}
