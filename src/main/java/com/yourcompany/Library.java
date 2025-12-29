package com.yourcompany;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Library {
    private Map<Integer, Book> books; // ключ - айди книги
    private List<Author> authors = new ArrayList<>();
    private static int totalBooks = 0;

    public Library() {
        this.books = new HashMap<>();
    }

    public void addBook(Book book) throws DuplicateBookException{
        if (books.containsKey(book.getId())){
            throw new DuplicateBookException("Книга \"" + book.getTitle() + "\" уже добавлена в библиотеку.");
        }
        books.put(book.getId(), book);
        totalBooks += 1;
    }

    @Override
    public String toString() {
        return "Library{" +
                " books=" + books +
                '}';
    }

    public static int getTotalBooks() {
        return totalBooks;
    }

    public void removeBook(int id){
        books.remove(id);
    }

    public void addAuthor(Author author){
        authors.add(author);
    }

    public void removeAuthor(String name){
        if (name == null) return;

        Iterator<Author> iterator = authors.iterator();
        while (iterator.hasNext()) {
            Author author = iterator.next();
            if (name.equals(author.getName())) {
                iterator.remove();
                break;
            }
        }
    }

    public void printAuthors() {
        authors.forEach(System.out::println);
    }

    public Optional<Book> findById(int id) throws BookNotFoundException {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> findBooksByAuthor(String authorName){
        if (authorName == null) {
            return Collections.emptyList();
        }
        return books.values().stream()
                .filter(n -> n.getAuthor().getName().equals(authorName)).collect(Collectors.toList());
    }

    public List<Book> findBooksByGenre(Genre genre){
        if (genre == null){
            return Collections.emptyList();
        }
        return books.values().stream()
                .filter(n -> n.getGenre().equals(genre)).collect(Collectors.toList());
    }

    public List<Book> findBooksByYear(int year){
        if (year < 0) {
            throw new IllegalArgumentException("Год не может быть отрицательным: " + year);
        }
        if (year > Year.now().getValue()) {
            throw new IllegalArgumentException("Год не может быть в будущем: " + year);
        }
        return books.values().stream()
                .filter(n -> n.getYear() == year).collect(Collectors.toList());
    }

    public void borrowBook(int id) throws BookNotFoundException {
        Book book = books.get(id);
        if (book.getStatus() != BookStatus.AVAILABLE){
            throw new BookNotFoundException(
                    "Книга не может быть выдана. Её текущий статус: " + book.getStatus().getDescription()
            );
        }
        book.setStatus(BookStatus.BORROWED);
    }

    public void returnBook(int id) throws BookNotFoundException {
        Book book = books.get(id);
        if (book.getStatus() != BookStatus.BORROWED){
            throw new IllegalStateException(
                    "Книга не была выдана. Её текущий статус: " + book.getStatus().getDescription()
            );
        }
        book.setStatus(BookStatus.AVAILABLE);
    }

}
