package com.yourcompany;

public record Genre(String name, String description) {
    public Genre {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название жанра не может быть пустым.");
        }
    }
}
