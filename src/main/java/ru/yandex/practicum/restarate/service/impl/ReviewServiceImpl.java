package ru.yandex.practicum.restarate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.restarate.model.Event;
import ru.yandex.practicum.restarate.model.EventOperation;
import ru.yandex.practicum.restarate.model.EventType;
import ru.yandex.practicum.restarate.model.Review;
import ru.yandex.practicum.restarate.service.DishService;
import ru.yandex.practicum.restarate.service.ReviewService;
import ru.yandex.practicum.restarate.service.UserService;
import ru.yandex.practicum.restarate.storage.ReviewStorage;
import ru.yandex.practicum.restarate.storage.impl.EventDbStorage;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final DishService dishService;
    private final EventDbStorage eventStorage;

    @Override
    public Review create(@Valid Review review) {
        userService.getUserById(review.getUserId());
        dishService.getDishById(review.getDishId());
        reviewStorage.create(review);
        eventStorage.addEvent(new Event(review.getUserId(), EventType.REVIEW, EventOperation.ADD, review.getReviewId()));
        return review;
    }

    @Override
    public Review update(@Valid Review review) {
        reviewStorage.getById(review.getReviewId());
        userService.getUserById(review.getUserId());
        dishService.getDishById(review.getDishId());

        Review updated = reviewStorage.update(review);
        eventStorage.addEvent(new Event(updated.getUserId(), EventType.REVIEW, EventOperation.UPDATE, updated.getReviewId()));
        return updated;
    }

    @Override
    public void delete(Long reviewId) {
        Long userId = reviewStorage.getById(reviewId).getUserId();
        reviewStorage.delete(reviewId);
        eventStorage.addEvent(new Event(userId, EventType.REVIEW, EventOperation.REMOVE, reviewId));
    }

    @Override
    public Review getById(Long reviewId) {
        return reviewStorage.getById(reviewId);
    }

    @Override
    public List<Review> getReviewList(Long filmId, Long count) {
        if (filmId != null) {
            dishService.getDishById(filmId);
        }
        return reviewStorage.getReviewList(filmId, count);
    }

    @Override
    public void likeReview(Long reviewId, Long userId) {
        reviewStorage.likeReview(reviewId, userId);
    }

    @Override
    public void dislikeReview(Long reviewId, Long userId) {
        reviewStorage.dislikeReview(reviewId, userId);
    }

    @Override
    public void deleteReviewLike(Long reviewId, Long userId) {
        reviewStorage.deleteReviewLike(reviewId, userId);
    }

    @Override
    public void deleteReviewDislike(Long reviewId, Long userId) {
        reviewStorage.deleteReviewDislike(reviewId, userId);
    }
}
