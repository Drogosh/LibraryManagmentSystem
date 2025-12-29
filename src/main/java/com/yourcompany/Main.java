import com.yourcompany.*;

void main() {
    System.out.println("=== Система управления библиотекой ===\n");

    // Создаем библиотеку
    Library library = new Library();

    // Создаем авторов
    System.out.println("1. Создаем авторов:");
    Author tolstoy = new Author("Лев Толстой", "Россия");
    Author dostoevsky = new Author("Фёдор Достоевский", "Россия");
    Author rowling = new Author("Джоан Роулинг", "Великобритания");
    Author king = new Author("Стивен Кинг", "США");
    Author anonymous = new Author("Неизвестный автор");

    System.out.println(tolstoy);
    System.out.println(dostoevsky);
    System.out.println(rowling);
    System.out.println(king);
    System.out.println(anonymous);
    System.out.println("Всего авторов создано: " + Author.getTotalAuthorsCreated() + "\n");

    // Создаем жанры (record)
    System.out.println("2. Создаем жанры:");
    Genre classic = new Genre("Классика", "Классическая литература");
    Genre fantasy = new Genre("Фэнтези", "Фентезийная литература");
    Genre horror = new Genre("Ужасы", "Ужасы и мистика");
    Genre novel = new Genre("Роман", "Романы разных эпох");

    System.out.println(classic);
    System.out.println(fantasy);
    System.out.println(horror);
    System.out.println(novel + "\n");

    // Создаем книги
    System.out.println("3. Добавляем книги в библиотеку:");
    Book warAndPeace = new Book("Война и мир", tolstoy, classic, 1869);
    Book crimeAndPunishment = new Book("Преступление и наказание", dostoevsky, novel, 1866);
    Book harryPotter = new Book("Гарри Поттер и философский камень", rowling, fantasy, 1997);
    Book it = new Book("Оно", king, horror, 1986);
    Book idiot = new Book("Идиот", dostoevsky, novel, 1869);
    Book shining = new Book("Сияние", king, horror, 1977);

    try {
        library.addBook(warAndPeace);
        library.addBook(crimeAndPunishment);
        library.addBook(harryPotter);
        library.addBook(it);
        library.addBook(idiot);
        library.addBook(shining);
        System.out.println("Книги успешно добавлены!");
    } catch (DuplicateBookException e) {
        System.err.println("Ошибка: " + e.getMessage());
    }
    System.out.println("Всего книг в библиотеке: " + library.getTotalBooks() + "\n");

    // Поиск по автору
    System.out.println("Книги Достоевского:");
    List<Book> dostoevskyBooks = library.findBooksByAuthor("Фёдор Достоевский");
    dostoevskyBooks.forEach(System.out::println);

    // Поиск по жанру
    System.out.println("\nКниги в жанре ужасы:");
    List<Book> horrorBooks = library.findBooksByGenre(horror);
    horrorBooks.forEach(System.out::println);

    // Поиск по году
    System.out.println("\nКниги 1869 года:");
    List<Book> books1869 = library.findBooksByYear(1869);
    books1869.forEach(System.out::println);

    // Выдача книги
    System.out.println("\n5. Выдача книги:");
    try {
        System.out.println("Пытаемся выдать 'Война и мир':");
        library.borrowBook(warAndPeace.getId());
        System.out.println("Книга успешно выдана!");
        System.out.println("Статус книги: " + warAndPeace.getStatus().getDescription());

        // Пытаемся выдать уже выданную книгу
        System.out.println("\nПытаемся снова выдать 'Война и мир':");
        library.borrowBook(warAndPeace.getId());
    } catch (BookNotFoundException | IllegalStateException e) {
        System.out.println("!!!Ошибка: " + e.getMessage());
    }

    System.out.println("\n6. Возврат книги:");
    try {
        library.returnBook(warAndPeace.getId());
        System.out.println("Книга успешно возвращена!");
        System.out.println("Статус книги: " + warAndPeace.getStatus().getDescription());
    } catch (BookNotFoundException | IllegalStateException e) {
        System.out.println("Ошибка: " + e.getMessage());
    }


}
