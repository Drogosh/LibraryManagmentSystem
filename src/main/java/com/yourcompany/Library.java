package com.yourcompany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private Map<Integer, Book> books; // ключ - айди книги

    public Library() {
        this.books = new HashMap<>();
    }

    public void addBook(Book book){
        books.put(book.getId(), book);
    }

    @Override
    public String toString() {
        return "Library{" +
                " books=" + books +
                '}';
    }
}
