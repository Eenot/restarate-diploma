package ru.yandex.practicum.restarate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Необходимо указать название")
    private String name;

    @Size(min = 1, max = 200, message = "Длинна описания (description) не может превышать 200 символов!")
    private String description;

    @NotNull(message = "Дата релиза (releaseDate) не должна быть пустой")
    private LocalDate releaseDate;

    @Positive(message = "Вес блюда (weight) должен быть положительным")
    private Long weight;
    @ManyToOne  // или @OneToOne, если это отношение "один к одному"
    @JoinColumn(name = "pricing_id")
    private Catalog pricing;
    @ManyToMany
    @JoinTable(
            name = "dish_category",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Catalog> categories;
    @ManyToMany
    @JoinTable(
            name = "dish_authors",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Catalog> authors;
}
