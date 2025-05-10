package ru.yandex.practicum.restarate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.restarate.model.Catalog;
import ru.yandex.practicum.restarate.service.PricingService;
import ru.yandex.practicum.restarate.storage.impl.PricingDbStorageImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final PricingDbStorageImpl ratingStorage;

    @Override
    public List<Catalog> getAll() {
        return ratingStorage.getAll();
    }

    @Override
    public Catalog getById(Long id) {
        return ratingStorage.getById(id);
    }
}
