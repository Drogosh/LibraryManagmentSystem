package com.yourcompany;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Library {
    private Map<Integer, Book> books; // ключ - айди книги
    private List<Author> authors = new ArrayList<>();
    private Map<String, Long> genreStatistics = new HashMap<>();
    private static int totalBooks = 0;

    public Library() {
        this.books = new HashMap<>();
    }

    public void addBook(Book book) throws DuplicateBookException{
        if (books.containsKey(book.getId())){
            throw new DuplicateBookException("Книга \"" + book.getTitle() + "\" уже добавлена в библиотеку.");
        }
        if (!authors.contains(book.getAuthor())){
            authors.add(book.getAuthor());
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

    public Map<String, Long> getGenreStatistics(){
        return books.values().stream()
                .collect(Collectors.groupingBy(
                        b->b.getGenre().name(),
                        Collectors.counting()
                ));
    }

    public List<Book> getBooksSortedByYear(){
        return books.values().stream()
                .sorted(Comparator.comparingInt(Book::getYear))
                .collect(Collectors.toList());
    }

    public Book getOldestBook(){
        return books.values().stream()
                .min(Comparator.comparing(Book::getYear))
                        .orElse(null);
    }

    public Book getNewestBook(){
        return books.values().stream()
                .max(Comparator.comparing(Book::getYear))
                .orElse(null);
    }

}
