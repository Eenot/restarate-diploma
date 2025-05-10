package ru.yandex.practicum.restarate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.service.impl.AuthorServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/authors")
@Slf4j
public class AuthorController {
    private final AuthorServiceImpl authorServiceImpl;

    public AuthorController(AuthorServiceImpl authorServiceImpl) {
        this.authorServiceImpl = authorServiceImpl;
    }

    @GetMapping
    public List<Catalog> getAll() {
        return authorServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public Catalog getById(@PathVariable Long id) {
        return authorServiceImpl.getById(id);
    }

    @PostMapping
    public Catalog createAuthor(@Valid @RequestBody Catalog body) {
        return authorServiceImpl.createAuthor(body);
    }

    @PutMapping
    public Catalog updateAuthor(@Valid @RequestBody Catalog body) {
        return authorServiceImpl.updateAuthor(body);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        authorServiceImpl.deleteById(id);
    }
}
