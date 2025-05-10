package ru.yandex.practicum.restarate.service;

import ru.yandex.practicum.restarate.model.Dish;
import ru.yandex.practicum.restarate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserService {
    List<User> getAll();

    User create(@Valid User user);

    User update(@Valid User user);

    void remove(Long id);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getUserFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);

    User getUserById(Long id);

    List<Dish> getRecommendations(Long userId);
}
