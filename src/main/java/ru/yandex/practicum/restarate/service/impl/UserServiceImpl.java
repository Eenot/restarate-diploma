package ru.yandex.practicum.restarate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.restarate.error.ValidationException;
import ru.yandex.practicum.restarate.model.Event;
import ru.yandex.practicum.restarate.model.EventOperation;
import ru.yandex.practicum.restarate.model.EventType;
import ru.yandex.practicum.restarate.model.Dish;
import ru.yandex.practicum.restarate.model.User;
import ru.yandex.practicum.restarate.service.UserService;
import ru.yandex.practicum.restarate.storage.DishStorage;
import ru.yandex.practicum.restarate.storage.UserStorage;
import ru.yandex.practicum.restarate.storage.impl.EventDbStorage;
import ru.yandex.practicum.restarate.utils.UserIdGenerator;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.restarate.utils.DefaultData.ENTITY_PROCESSED_SUCCESSFUL;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final DishStorage dishStorage;
    private final EventDbStorage eventStorage;

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User create(User user) {
        validateUser(user);
        userStorage.save(user);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, user);
        return user;
    }

    @Override
    public User update(User user) {
        validateUser(user);
        userStorage.update(user);
        log.debug(ENTITY_PROCESSED_SUCCESSFUL, user);
        return user;
    }

    @Override
    public void remove(Long id) {
        userStorage.removeUserById(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        userStorage.addFriendship(userId, friendId);
        eventStorage.addEvent(new Event(userId, EventType.FRIEND, EventOperation.ADD, friendId));

    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        userStorage.removeFriendship(userId, friendId);
        eventStorage.addEvent(new Event(userId, EventType.FRIEND, EventOperation.REMOVE, friendId));
    }

    @Override
    public List<User> getUserFriends(Long id) {
        return userStorage.getUserFriends(id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> collection = userStorage.getUserFriends(id);
        List<User> secondUser = userStorage.getUserFriends(otherId);
        collection.retainAll(secondUser);
        return collection;
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<Dish> getRecommendations(Long userId) {
        return dishStorage.getRecommendations(userId);
    }

    private void validateUser(@Valid User user) {
        if (user.getLogin().chars().anyMatch(Character::isWhitespace)) {
            ValidationException e = new ValidationException("Логин не может быть пустым и содержать пробелы!");
            log.warn(e.getMessage());
            throw e;
        }
        if (user.getId() == null) {
            user.setId(UserIdGenerator.getInstance().getId());
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
