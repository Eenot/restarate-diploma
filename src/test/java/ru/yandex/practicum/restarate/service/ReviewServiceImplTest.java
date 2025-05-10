package ru.yandex.practicum.restarate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.restarate.model.Review;
import ru.yandex.practicum.restarate.service.impl.ReviewServiceImpl;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ReviewServiceImplTest {

    @Autowired
    private ReviewServiceImpl reviewService;

    @Test
    void validateContent() {
        var review = getDefaultReview();
        review.setContent(null);

        String expectedMsg = "Необходимо заполнить content";
        var result = assertThrows(ConstraintViolationException.class, () -> reviewService.create(review));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateIsPositive() {
        var review = getDefaultReview();
        review.setIsPositive(null);

        String expectedMsg = "Необходимо заполнить isPositive";
        var result = assertThrows(ConstraintViolationException.class, () -> reviewService.create(review));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateUser() {
        var review = getDefaultReview();
        review.setUserId(null);

        String expectedMsg = "Необходимо заполнить userId";
        var result = assertThrows(ConstraintViolationException.class, () -> reviewService.create(review));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    @Test
    void validateDish() {
        var review = getDefaultReview();
        review.setDishId(null);

        String expectedMsg = "Необходимо заполнить dishId";
        var result = assertThrows(ConstraintViolationException.class, () -> reviewService.create(review));
        assertEquals(expectedMsg, getErrorMessage(result.getMessage()));
    }

    private Review getDefaultReview() {
        return new Review(1L, "content", Boolean.TRUE, 1L, 2L, 0L);
    }

    private String getErrorMessage(String fullMsg) {
        return fullMsg.split(":")[1].substring(1);
    }

}
