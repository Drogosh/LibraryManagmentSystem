package com.yourcompany;

public enum BookStatus {
    AVAILABLE("Доступна"),
    BORROWED("Выдана"),
    RESERVED("Зарезервирована");

    private final String description;

    BookStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
