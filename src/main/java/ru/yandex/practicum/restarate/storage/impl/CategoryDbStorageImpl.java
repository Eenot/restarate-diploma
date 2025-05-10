package ru.yandex.practicum.restarate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.restarate.error.NotExistException;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.storage.CategoryStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CategoryDbStorageImpl implements CategoryStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Catalog> getAll() {
        String sql = "SELECT * FROM category;";
        return jdbcTemplate.query(sql, (rs, row) -> mapRow(rs));
    }

    @Override
    public Catalog getById(Long id) {
        String sql = "SELECT * FROM category WHERE id = ?;";
        var result = jdbcTemplate.query(sql, (rs, row) -> mapRow(rs), id).stream().findFirst();
        if (result.isEmpty()) {
            throw new NotExistException(id.toString());
        }
        return result.get();
    }

    @Override
    public List<Catalog> getCategories(Long dishId) {

        Set<Catalog> categories = new HashSet<>(jdbcTemplate.query("SELECT c.id, c.name " + "FROM dish_category AS dc " + "LEFT OUTER JOIN category AS c ON c.id = dc.category_id " + "WHERE dc.dish_id = ? " + "ORDER BY c.id", (rs, row) -> mapRow(rs), dishId));
        return new ArrayList<>(categories);
    }

    private Catalog mapRow(ResultSet rs) throws SQLException {
        return new Catalog(rs.getLong("id"), rs.getString("name"));
    }
}
