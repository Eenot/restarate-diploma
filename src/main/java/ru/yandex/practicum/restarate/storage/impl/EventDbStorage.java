package ru.yandex.practicum.restarate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.restarate.model.Event;
import ru.yandex.practicum.restarate.model.EventOperation;
import ru.yandex.practicum.restarate.model.EventType;
import ru.yandex.practicum.restarate.storage.EventStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addEvent(Event event) {

        String sqlQuery = "INSERT INTO events (add_timestamp, user_id, events_type_id, events_operation_id, entity_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                event.getAddTimeStamp(),
                event.getUserId(),
                event.getEventType().getId(),
                event.getOperation().getId(),
                event.getEntityId());
    }


    @Override
    public List<Event> getEventsByUserId(Long userId) {

        String sqlQuery = "SELECT e.events_id, e.add_timestamp, e.user_id, e.entity_id, " +
                "et.events_name, eo.operation_name FROM events AS e " +
                "LEFT JOIN events_type AS et ON e.events_type_id = et.events_type_id " +
                "LEFT JOIN events_operation AS eo ON e.events_operation_id = eo.events_operation_id " +
                "WHERE e.user_id=?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToEvent, userId);
    }


    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {

        Event event = new Event();

        event.setEventId(rs.getLong("events_id"));
        event.setAddTimeStamp(rs.getLong("add_timestamp"));
        event.setUserId(rs.getLong("user_id"));
        event.setEventType(EventType.valueOf(rs.getString("events_name")));
        event.setOperation(EventOperation.valueOf(rs.getString("operation_name")));
        event.setEntityId(rs.getLong("entity_id"));

        return event;
    }
}
