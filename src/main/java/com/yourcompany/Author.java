package com.yourcompany;

import java.util.Objects;

public class Author implements Comparable<Author>{

    private static int counter = 0;
    private final int id;
    private  final String name;
    private final String country;

    public Author(String name, String country) {
        this.name = validateName(name);
        this.country = country == null ? "Нет данных." : country;
        this.id = ++counter;
    }

    public Author(String name) {
        this(name, "Нет данных");
    }

    public String validateName(String name){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя автора не может быть пустым");
        }
        return name;
    }

    public static int getTotalAuthorsCreated() {
        return counter;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return  " имя автора: '" + name + '\'' +
                ", страна: '" + country + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public int compareTo(Author o) {
        if (o == null ){
            throw new NullPointerException("Нельзя сравнить пустого автора.");
        }
        return this.name.compareTo(o.name);
    }

}
