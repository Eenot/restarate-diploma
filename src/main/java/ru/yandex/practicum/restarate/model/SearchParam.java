package ru.yandex.practicum.restarate.model;

import lombok.Getter;

import javax.persistence.Entity;

@Getter
public enum SearchParam {

    AUTHOR,
    TITLE;

    public static SearchParam getParam(String code) {
        switch (code) {
            case "author":
                return AUTHOR;
            case "title":
                return TITLE;
            default:
                return null;
        }
    }

}
