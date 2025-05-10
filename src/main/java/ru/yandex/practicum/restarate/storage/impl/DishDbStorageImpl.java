package ru.yandex.practicum.restarate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.restarate.error.NotExistException;
import ru.yandex.practicum.restarate.error.ValidationException;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.model.Dish;
import ru.yandex.practicum.restarate.model.SearchParam;
import ru.yandex.practicum.restarate.storage.DishStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DishDbStorageImpl implements DishStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Dish> getAll() {
        String sql = "SELECT d.*,\n" +
                "       p.name as pricing_name\n" +
                "FROM dish d\n" +
                "         left join pricing p on d.pricing = p.id;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDish(rs));
    }

    @Override
    public Dish save(Dish dish) {
        String sql = "insert into DISH(NAME, DESCRIPTION, RELEASE_DATE, WEIGHT, PRICING) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, dish.getName());
            stmt.setString(2, dish.getDescription());
            stmt.setDate(3, Date.valueOf(dish.getReleaseDate()));
            stmt.setLong(4, dish.getWeight());
            if (dish.getPricing() != null) {
                stmt.setLong(5, dish.getPricing().getId());
            } else {
                stmt.setObject(5, null);
            }
            return stmt;
        }, keyHolder);

        var id = keyHolder.getKey().longValue();
        if (dish.getCategories() != null && !dish.getCategories().isEmpty()) {
            updateCategories(id, dish.getCategories());
        }
        if (dish.getAuthors() != null && !dish.getAuthors().isEmpty()) {
            updateDishAuthors(id, dish.getAuthors());
        }

        return dish;
    }

    @Override
    public Dish update(Dish dish) {
        getDishById(dish.getId());

        String sql = "UPDATE dish\n" +
                "SET name         = ?,\n" +
                "    description  = ?,\n" +
                "    release_date = ?,\n" +
                "    weight     = ?,\n" +
                "    pricing       = ?\n" +
                "WHERE id = ?;";
        jdbcTemplate.update(sql,
                dish.getName(),
                dish.getDescription(),
                dish.getReleaseDate(),
                dish.getWeight(),
                dish.getPricing().getId(),
                dish.getId());

        if (dish.getCategories() == null || dish.getCategories().isEmpty()) {
            deleteCategories(dish.getId());
        } else {
            deleteCategories(dish.getId());
            updateCategories(dish.getId(), dish.getCategories());
        }

        if (dish.getAuthors() == null || dish.getAuthors().isEmpty()) {
            deleteDishAuthors(dish.getId());
        } else {
            deleteDishAuthors(dish.getId());
            updateDishAuthors(dish.getId(), dish.getAuthors());
        }
        return getDishById(dish.getId());
    }

    @Override
    public void removeDishById(Long id) {
        String sql = "DELETE FROM dish WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Dish getDishById(Long id) {
        String sql = "SELECT d.*,\n" +
                "       p.name as pricing_name\n" +
                "FROM dish d\n" +
                "         join pricing p on d.pricing = p.id\n" +
                "WHERE d.id = ?;";
        var result = jdbcTemplate.query(sql, (rs, rowNum) -> makeDish(rs), id).stream().findFirst();
        if (result.isEmpty()) {
            throw new NotExistException(id.toString());
        }
        return result.get();
    }

    @Override
    public List<Dish> getPopularDishes(Long count, Long categoryId, Integer year) {
        List<Dish> popularDishes;
        String yearFilter = "WHERE YEAR(d.release_date) = ? ";
        String categoryFilter = "WHERE dc.category_id = ? ";
        String categoryJoin = "JOIN dish_category dc ON d.id = dc.dish_id ";
        String categoryAndYearFilter = "WHERE dc.category_id = ? AND YEAR(d.release_date) = ? ";
        StringBuilder sql = new StringBuilder("SELECT d.*, p.name as pricing_name\n" +
                "FROM dish d JOIN pricing p ON d.pricing = p.id\n" +
                "LEFT JOIN favorite_dishes fd on d.id = fd.dish_id ");
        String sqlEnd = "GROUP BY d.id ORDER BY count(fd.dish_id) DESC LIMIT ?";

        if (categoryId == null && year == null) {
            String sqlString = sql.append(sqlEnd).toString();
            popularDishes = jdbcTemplate.query(sqlString, (rs, rowNum) -> makeDish(rs), count);
        } else if (categoryId == null) {
            String sqlString = sql.append(yearFilter).append(sqlEnd).toString();
            popularDishes = jdbcTemplate.query(sqlString, (rs, rowNum) -> makeDish(rs), year, count);
        } else if (year == null) {
            String sqlString = sql.append(categoryJoin).append(categoryFilter).append(sqlEnd).toString();
            popularDishes = jdbcTemplate.query(sqlString, (rs, rowNum) -> makeDish(rs), categoryId, count);
        } else {
            String sqlString = sql.append(categoryJoin).append(categoryAndYearFilter).append(sqlEnd).toString();
            popularDishes = jdbcTemplate.query(sqlString, (rs, rowNum) -> makeDish(rs), categoryId, year, count);
        }
        return popularDishes;
    }

    @Override
    public List<Dish> getRecommendations(Long userId) {

        String sqlQuery =
                "SELECT d.*, p.name as pricing_name FROM dish AS d " +
                        "JOIN pricing AS p ON d.pricing = p.id WHERE d.id IN (" +
                        "SELECT dish_id FROM favorite_dishes WHERE user_id IN (" +
                        "SELECT fd.user_id FROM favorite_dishes AS fd " +
                        "LEFT JOIN favorite_dishes AS fd2 ON fd2.dish_id = fd.dish_id " +
                        "GROUP BY fd.user_id, fd2.user_id " +
                        "HAVING fd.user_id IS NOT NULL AND fd.user_id != ? AND fd2.user_id = ? " +
                        "ORDER BY COUNT(fd.user_id) DESC) AND dish_id NOT IN (" +
                        "SELECT dish_id FROM favorite_dishes WHERE user_id = ?))";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeDish(rs), userId, userId, userId);
    }

    @Override
    public List<Dish> search(String query, String by) {
        List<SearchParam> searchParams = getSearchParams(by);

        String sql = "SELECT d.*," +
                "       p.name as pricing_name" +
                " FROM dish d" +
                "         LEFT JOIN pricing p on d.pricing = p.id" +
                "         LEFT JOIN FAVORITE_DISHES fd on d.ID = fd.DISH_ID";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        if (searchParams.isEmpty()) throw new ValidationException("Отсутствуют параметры сортировки");

        if (searchParams.contains(SearchParam.AUTHOR) && searchParams.contains(SearchParam.TITLE)) {
            sqlBuilder.append(String.format(" WHERE lower(d.NAME) like lower ('%%%s%%')" +
                    "  OR d.id in (" +
                    "    SELECT dish_id" +
                    "    FROM DISH_AUTHORS" +
                    "    WHERE AUTHOR_ID in (SELECT id from AUTHORS WHERE lower(NAME) like lower ('%%%s%%')))", query, query));
        } else {
            if (searchParams.contains(SearchParam.AUTHOR)) {
                sqlBuilder.append(String.format(" WHERE d.id in (" +
                        "    SELECT dish_id" +
                        "    FROM DISH_AUTHORS" +
                        "    WHERE AUTHOR_ID in (SELECT id from AUTHORS WHERE lower(NAME) like lower ('%%%s%%')))", query));
            } else {
                sqlBuilder.append(String.format(" WHERE lower(d.NAME) like lower ('%%%s%%')", query));
            }
        }
        sqlBuilder.append(" GROUP BY d.id ORDER BY count(fd.dish_id) DESC");
        return jdbcTemplate.query(sqlBuilder.toString(), (rs, rowNum) -> makeDish(rs));
    }


    @Override
    public List<Dish> getDishesByUser(Long userId) {
        String sql = "SELECT d.*, p.name AS pricing_name\n" +
                "from DISH d\n" +
                "JOIN PRICING P on d.PRICING = P.ID\n" +
                "JOIN FAVORITE_DISHES fd on d.ID = fd.DISH_ID\n" +
                "WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDish(rs), userId);
    }

    public void likeDish(Long userId, Long dishId) {
        String sql = "INSERT INTO favorite_dishes(user_id, dish_id)" +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, dishId);
    }

    public void dislikeDish(Long userId, Long dishId) {
        String sql = "DELETE FROM favorite_dishes WHERE user_id = ? and dish_id = ?";
        jdbcTemplate.update(sql, userId, dishId);
    }

    private void updateCategories(Long dishId, List<Catalog> categories) {
        String secondSql = "INSERT INTO dish_category VALUES(?, ?);";
        categories.forEach(categoryId -> jdbcTemplate.update(secondSql, dishId, categoryId.getId()));
    }

    private void updateDishAuthors(Long dishId, List<Catalog> authors) {
        String sqlQuery = "INSERT INTO dish_authors VALUES(?, ?);";
        authors.forEach(authorId -> jdbcTemplate.update(sqlQuery, dishId, authorId.getId()));
    }

    private void deleteDishAuthors(Long dishId) {
        String sql = "DELETE FROM dish_authors WHERE dish_id = ?;";
        jdbcTemplate.update(sql, dishId);
    }

    private void deleteCategories(Long dishId) {
        String sql = "DELETE FROM dish_category WHERE dish_id = ?;";
        jdbcTemplate.update(sql, dishId);
    }

    private Dish makeDish(ResultSet rs) throws SQLException {
        return new Dish(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getLong("weight"),
                new Catalog(rs.getLong("pricing"), rs.getString("pricing_name")),
                getDishCategories(rs.getLong("id")),
                getDishAuthors(rs.getLong("id"))
        );
    }

    private List<Catalog> getDishAuthors(Long dishId) {
        String sqlQuery = "SELECT * FROM authors WHERE id IN " +
                "(SELECT author_id FROM dish_authors WHERE dish_id = ?)";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, (rs, row) -> mapRow(rs), dishId));
    }

    private List<Catalog> getDishCategories(Long dishId) {
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

    private List<SearchParam> getSearchParams(String by) {
        return Arrays.stream(by.split(","))
                .distinct()
                .map(SearchParam::getParam)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
