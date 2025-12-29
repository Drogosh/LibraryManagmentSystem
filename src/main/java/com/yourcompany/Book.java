package com.yourcompany;

import java.time.LocalDate;
import java.util.Objects;

public class Book {
    //    id, title, author, genre, year, isBorrowed
    private final int id;
    private final String title;
    private final Author author;
    private final Genre genre;
    private final int year;
    private BookStatus status;
    private static int counter = 0;

    public Book(String title, Author author, Genre genre, int year) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустое.");
        }
        if (author == null) {
            throw new IllegalArgumentException("Автор должен быть указан.");
        }
        if (year < 0 || year > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("Год не может быть отрицательным!");
        }
        if (genre == null) {
            throw new IllegalArgumentException("Жанр должен быть указан.");
        }
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        id = ++counter;
        status = BookStatus.AVAILABLE;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", genre=" + genre +
                ", year=" + year +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void borrow(){
        if (status != BookStatus.AVAILABLE){
            throw new IllegalStateException(
                    "Книга не может быть выдана. Её текущий статус: " + status.getDescription()
            );
        }
        status = BookStatus.BORROWED;
    }

    public void returnBook(){
        if (status != BookStatus.BORROWED){
            throw new IllegalStateException(
                    "Книга не была выдана. Её текущий статус: " + status.getDescription()
            );
        }
        status = BookStatus.AVAILABLE;
    }

    public void reserve(){
        if (status != BookStatus.AVAILABLE){
            throw new IllegalStateException(
                    "Книга не может быть зарезервирована. Её текущий статус: " + status.getDescription()
            );
        }
        status = BookStatus.RESERVED;
    }
}

