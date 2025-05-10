package ru.yandex.practicum.restarate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    @NotNull(message = "Необходимо заполнить content")
    private String content;
    @NotNull(message = "Необходимо заполнить isPositive")
    private Boolean isPositive;
    @NotNull(message = "Необходимо заполнить userId")
    private Long userId;
    @NotNull(message = "Необходимо заполнить dishId")
    private Long dishId;
    private Long useful;

}
