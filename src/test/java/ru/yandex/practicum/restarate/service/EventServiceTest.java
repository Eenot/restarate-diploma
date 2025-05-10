package ru.yandex.practicum.restarate.service;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.restarate.model.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventServiceTest {

    private final UserService userService;
    private final DishService dishService;
    private final EventService eventService;



    @Test
    public void shouldCreateEvents() {

        User user1 = new User(1L, "dude@ya.ru",
                "mikey", "mikey", LocalDate.of(1990,1,1));

        userService.create(user1);

        User user2 = new User(2L, "mail@ya.ru",
                "user", "meh", LocalDate.of(1990,1,1));

        userService.create(user2);

        Dish dish = new Dish(1L, "Dish", "something",
                LocalDate.of(2001,1,1), 120L,
                new Catalog(3L, "$$$"), List.of(new Catalog(2L, "Напиток")), null);


        dishService.create(dish);

        Event event1 = new Event(user1.getId(), EventType.LIKE, EventOperation.ADD, dish.getId());
        Event event2 = new Event(user2.getId(), EventType.FRIEND, EventOperation.ADD, user1.getId());

        eventService.addEvent(event1);
        eventService.addEvent(event2);

        List<Event> user1Events = eventService.getEventsByUserId(user1.getId());
        List<Event> user2Events = eventService.getEventsByUserId(user2.getId());

        assertEquals(1, user1Events.size());
        assertEquals(1, user2Events.size());

        assertEquals(eventService.getEventsByUserId(user1.getId()).get(0).getUserId(), user1.getId());
        assertEquals(eventService.getEventsByUserId(user2.getId()).get(0).getUserId(), user2.getId());

        assertEquals(eventService.getEventsByUserId(user1.getId()).get(0).getEventType(), EventType.LIKE);
        assertEquals(eventService.getEventsByUserId(user1.getId()).get(0).getOperation(), EventOperation.ADD);

        assertEquals(eventService.getEventsByUserId(user2.getId()).get(0).getEventType(), EventType.FRIEND);
        assertEquals(eventService.getEventsByUserId(user2.getId()).get(0).getOperation(), EventOperation.ADD);

    }
}
