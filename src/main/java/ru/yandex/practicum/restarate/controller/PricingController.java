package ru.yandex.practicum.restarate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.service.impl.PricingServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/pricing")
public class PricingController {

    private final PricingServiceImpl pricingServiceImpl;

    public PricingController(PricingServiceImpl pricingServiceImpl) {
        this.pricingServiceImpl = pricingServiceImpl;
    }

    @GetMapping
    public List<Catalog> getAll() {
        return pricingServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public Catalog getById(@PathVariable Long id) {
        return pricingServiceImpl.getById(id);
    }
}
