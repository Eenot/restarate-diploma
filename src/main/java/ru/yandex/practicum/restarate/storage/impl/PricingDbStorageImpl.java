package ru.yandex.practicum.restarate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.restarate.error.NotExistException;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.storage.CatalogStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PricingDbStorageImpl implements CatalogStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Catalog> getAll() {
        String sql = "SELECT * FROM pricing;";
        return jdbcTemplate.query(sql, (rs, row) -> mapRow(rs));
    }

    @Override
    public Catalog getById(Long id) {
        String sql = "SELECT * FROM pricing WHERE id = ?";
        var result = jdbcTemplate.query(sql, (rs, row) -> mapRow(rs), id).stream().findFirst();
        if (result.isEmpty()) {
            throw new NotExistException(id.toString());
        }
        return result.get();
    }

    private Catalog mapRow(ResultSet rs) throws SQLException {
        return new Catalog(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
