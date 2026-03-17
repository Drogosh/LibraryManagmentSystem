package com.yourcompany;

import com.yourcompany.exception.BookNotFoundException;
import com.yourcompany.exception.DuplicateBookException;

import javax.xml.transform.Source;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleApp {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Загружаем тестовые данные
        initializeTestData();

        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> addNewBook();
                case 2 -> listAllBooks();
                case 3 -> findBookById();
                case 4 -> findBooksByAuthor();
                case 5 -> findBooksByGenre();
                case 6 -> findBooksByYear();
                case 7 -> borrowBook();
                case 8 -> returnBook();
                case 9 -> reserveBook();
                case 10 -> showStatistics();
                case 11 -> showAllAuthors();
                case 12 -> removeBook();
                case 13 -> searchBooksByPredicate();
                case 0 -> {
                    System.out.println("👋 До свидания!");
                    running = false;

                }
                default -> System.out.println("❌ Неверный выбор. Попробуйте снова (0-13).");
            }

            if (running && choice != 0) {
                System.out.println("\n🔹 Нажмите Enter для продолжения...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📚 СИСТЕМА УПРАВЛЕНИЯ БИБЛИОТЕКОЙ");
        System.out.println("=".repeat(60));
        System.out.println("1.  ➕ Добавить новую книгу");
        System.out.println("2.  📖 Показать все книги");
        System.out.println("3.  🔍 Найти книгу по ID");
        System.out.println("4.  👤 Найти книги по автору");
        System.out.println("5.  🏷️ Найти книги по жанру");
        System.out.println("6.  📅 Найти книги по году");
        System.out.println("7.  📤 Выдать книгу");
        System.out.println("8.  📥 Вернуть книгу");
        System.out.println("9.  🔖 Зарезервировать книгу");
        System.out.println("10. 📊 Показать статистику");
        System.out.println("11. ✍️ Показать всех авторов");
        System.out.println("12. 🗑️ Удалить книгу");
        System.out.println("13. 🔎 Расширенный поиск (Predicate)");
        System.out.println("0.  🚪 Выход");
        System.out.println("=".repeat(60));
        System.out.print("Ваш выбор: ");
    }

    private static int getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return choice;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addNewBook() {
        System.out.println("\n--- ➕ Добавление новой книги ---");

        try {
            // Ввод названия
            System.out.print("Введите название книги: ");
            String title = scanner.nextLine();

            // Выбор или создание автора
            Author author = selectOrCreateAuthor();

            // Выбор или создание жанра
            Genre genre = selectOrCreateGenre();

            // Ввод года
            System.out.print("Введите год издания: ");
            int year = Integer.parseInt(scanner.nextLine());

            // Создаем книгу
            Book book = new Book(title, author, genre, year);

            // Добавляем в библиотеку
            library.addBook(book);

            System.out.println("✅ Книга успешно добавлена!");
            System.out.println("   ID книги: " + book.getId());
            System.out.println("   Название: " + book.getTitle());

        } catch (DuplicateBookException e) {
            System.err.println("❌ Ошибка: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Ошибка: Неверный формат года");
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Ошибка валидации: " + e.getMessage());
        }
    }

    private static void listAllBooks() {
        System.out.println("\n--- 📖 Все книги в библиотеке ---");
        List<Book> books = library.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("📭 В библиотеке нет книг");
            return;
        }

        System.out.println("Всего книг: " + books.size() + "\n");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.printf("%d. %s\n", i + 1, book);
        }
    }

    private static void findBookById() {
        System.out.println("\n--- 🔍 Поиск книги по ID ---");

        try {
            System.out.print("Введите ID книги: ");
            int id = Integer.parseInt(scanner.nextLine());

            Book book = library.findById(id);
            System.out.println("\n✅ Найдена книга:");
            System.out.println(book);

            // Дополнительная информация
            System.out.println("\n📊 Детали:");
            System.out.println("   Доступна: " + (book.isAvailable() ? "✅ Да" : "❌ Нет"));
            System.out.println("   Статус: " + book.getStatus().getDescription());

        } catch (BookNotFoundException e) {
            System.err.println("❌ " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Неверный формат ID");
        }
    }

    private static void findBooksByAuthor() {
        System.out.println("\n--- 👤 Поиск книг по автору ---");

        System.out.print("Введите имя автора: ");
        String authorName = scanner.nextLine();

        List<Book> books = library.findBooksByAuthor(authorName);

        if (books.isEmpty()){
            System.out.println("📭 Книги автора \"" + authorName + "\" не найдены");
            return;
        }

        System.out.println("\n📚 Найдено книг: " + books.size());
        for (int i = 0; i < books.size(); i++){
//            System.out.println(books.get(i));
            Book book = books.get(i);
            System.out.printf("%d. %s (%d) - %s\n",
                    i + 1, book.getTitle(), book.getYear(), book.getStatus().getDescription());
        }
    }

    public static void findBooksByGenre(){
        System.out.println("\n--- 🏷️ Поиск книг по жанру ---");

        System.out.print("Введите название жанра: ");

        Genre selectedGenre = selectGenre();
        if (selectedGenre == null) return;

        List<Book> books = library.findBooksByGenre(selectedGenre);

        if (books.isEmpty()) {
            System.out.println("📭 Книги в жанре \"" + selectedGenre.name() + "\" не найдены");
            return;
        }

        System.out.println("\n📚 Найдено книг: " + books.size());
        for (Book book : books) {
            System.out.println("   • " + book.getTitle() + " - " + book.getAuthor().getName());
        }
    }

    private static void findBooksByYear() {
        System.out.println("\n--- 📅 Поиск книг по году ---");
        int year = Integer.parseInt(scanner.nextLine());

        try{
            List<Book> books = library.findBooksByYear(year);

            if (books.isEmpty()){
                System.out.println("📭 Книги за " + year + " год не найдены");
                return;
            }

            System.out.println("\n📚 Книги за " + year + " год:");

            for (Book book: books){
                System.out.println("   • " + book.getTitle() + " - " + book.getAuthor().getName());
            }
        } catch (NumberFormatException e){
            System.out.println("❌ Неверный формат года");
        } catch (IllegalArgumentException e){
            System.out.println("❌ " + e.getMessage());
        }

    }

    public static void borrowBook(){
        System.out.println("\n--- 📤 Выдача книги ---");

        try {
            System.out.println("Введите ID книги для выдачи: ");
            int id = Integer.parseInt(scanner.nextLine());

            Book book = library.findById(id);

            System.out.println("\n📖 Книга: " + book.getTitle());
            System.out.println("Текущий статус: " + book.getStatus().getDescription());

            if (book.isAvailable()){
                library.borrowBook(id);
                System.out.println("✅ Книга успешно выдана!");
                System.out.println("   Новый статус: " + book.getStatus().getDescription());
            } else {
                System.out.println("❌ Книга недоступна для выдачи");
            }
        } catch (BookNotFoundException e) {
            System.err.println("❌ " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Неверный формат ID");
        } catch (IllegalStateException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }

    public static void returnBook(){
        System.out.println("\n--- 📤 Выдача книги ---");

        try {

            System.out.println("Введите ID книги для возврата: ");
            int id = Integer.parseInt(scanner.nextLine());
            Book book = library.findById(id);

            if (book.isBorrowed()){
                library.returnBook(id);
                System.out.println("✅ Книга успешно возвращена!");
                System.out.println("   Новый статус: " + book.getStatus().getDescription());
            } else {
                System.out.println("❌ Книга не была выдана");
            }

        } catch (BookNotFoundException e) {
            System.err.println("❌ " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Неверный формат ID");
        } catch (IllegalStateException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }
    public static void reserveBook(){
        System.out.println("\n--- 🔖 Резервирование книги ---");

        try {
            System.out.println("Введите ID книги для возврата: ");
            int id = Integer.parseInt(scanner.nextLine());

            Book book = library.findById(id);

            if (book.isAvailable()){
                library.reserveBook(id);
                System.out.println("✅ Книга успешно зарезервирована!");
                System.out.println("   Новый статус: " + book.getStatus().getDescription());
            } else {
                System.out.println("❌ Книга недоступна для резервирования");
            }
        } catch (BookNotFoundException e) {
            System.err.println("❌ " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Неверный формат ID");
        } catch (IllegalStateException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }
    public static void showStatistics(){
        System.out.println("\n--- 📊 Статистика библиотеки ---");
        System.out.println("\n📊 ОБЩАЯ ИНФОРМАЦИЯ:");
        System.out.println("   Всего книг: " + library.getTotalBooks());
        System.out.println("   Доступно: " + library.getAvailableBooksCount());
        System.out.println("   Выдано: " + library.getBorrowedBooksCount());
        System.out.println("   Зарезервировано: " + library.getReservedBooksCount());

        System.out.println("\n📈 СТАТИСТИКА ПО ЖАНРАМ:");
        Map<String, Long> genreStats = library.getGenreStatistics();
        if (genreStats.isEmpty()) {
            System.out.println("   Нет данных");
        } else {
            genreStats.forEach((genre, count) ->
                    System.out.printf("   %s: %d шт.\n", genre, count));
        }

        System.out.println("\n📚 САМАЯ СТАРАЯ КНИГА:");
        Book oldest = library.getOldestBook();
        if (oldest != null) {
            System.out.println("   " + oldest.getTitle() + " (" + oldest.getYear() + ")");
        } else {
            System.out.println("   Нет данных");
        }

        System.out.println("\n📚 САМАЯ НОВАЯ КНИГА:");
        Book newest = library.getNewestBook();
        if (newest != null) {
            System.out.println("   " + newest.getTitle() + " (" + newest.getYear() + ")");
        } else {
            System.out.println("   Нет данных");
        }

        System.out.println("\n📚 КНИГИ ПО ГОДАМ:");
        List<Book> sortedByYear = library.getBooksSortedByYear();
        if (!sortedByYear.isEmpty()) {
            sortedByYear.forEach(book ->
                    System.out.printf("   %d: %s\n", book.getYear(), book.getTitle()));
        }
    }
    public static void showAllAuthors(){
        System.out.println("\n--- ✍️ Все авторы в библиотеке ---");

        List<Author> authors = library.getAllBooks().stream()
                .map(Book::getAuthor)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        if (authors.isEmpty()){
            System.out.println("📭 Нет авторов");
            return;
        }

        System.out.println("Всего авторов: " + authors.size() + "\n");
        for (int i = 0; i < authors.size(); i++){
            Author author = authors.get(i);
            System.out.printf("%d. %s (страна: %s)\n",
                    i + 1, author.getName(), author.getCountry());

            long bookCount = library.findBooksByAuthor(author.getName()).size();
            System.out.printf("   📚 Книг в библиотеке: %d\n", bookCount);
        }
    }
    public static void removeBook(){
        System.out.println("\n--- 🗑️ Удаление книги ---");


    }
    public static void searchBooksByPredicate(){

    }


    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private static Author selectOrCreateAuthor() {
        System.out.println("\nВыберите автора:");

        // Получаем всех авторов из библиотеки
        List<Author> existingAuthors = library.getAllBooks().stream()
                .map(Book::getAuthor)
                .distinct()
                .collect(java.util.stream.Collectors.toList());

        if (!existingAuthors.isEmpty()) {
            System.out.println("0. ➕ Создать нового автора");
            for (int i = 0; i < existingAuthors.size(); i++) {
                Author author = existingAuthors.get(i);
                System.out.printf("%d. %s (%s)\n", i + 1, author.getName(), author.getCountry());
            }

            System.out.print("Выберите автора (0 для нового): ");
            String choice = scanner.nextLine();

            try {
                int index = Integer.parseInt(choice);
                if (index > 0 && index <= existingAuthors.size()) {
                    return existingAuthors.get(index - 1);
                }
            } catch (NumberFormatException e) {
                // Продолжаем создание нового автора
            }
        }

        // Создание нового автора
        System.out.println("\n--- Создание нового автора ---");
        System.out.print("Введите имя автора: ");
        String authorName = scanner.nextLine();

        System.out.print("Введите страну автора (Enter для пропуска): ");
        String country = scanner.nextLine();

        if (country.isEmpty()) {
            return new Author(authorName);
        } else {
            return new Author(authorName, country);
        }
    }


    private static Genre selectOrCreateGenre() {
        System.out.println("\nВыберите жанр:");
        System.out.println("1. Классика");
        System.out.println("2. Фэнтези");
        System.out.println("3. Ужасы");
        System.out.println("4. Роман");
        System.out.println("5. ➕ Создать новый жанр");

        System.out.print("Выберите жанр (1-5): ");
        String choice = scanner.nextLine();

        return switch (choice) {
            case "1" -> new Genre("Классика", "Классическая литература");
            case "2" -> new Genre("Фэнтези", "Фэнтезийная литература");
            case "3" -> new Genre("Ужасы", "Ужасы и мистика");
            case "4" -> new Genre("Роман", "Романы разных эпох");
            case "5" -> {
                System.out.print("Введите название жанра: ");
                String genreName = scanner.nextLine();
                System.out.print("Введите описание жанра: ");
                String genreDesc = scanner.nextLine();
                yield new Genre(genreName, genreDesc);
            }
            default -> {
                System.err.println("❌ Неверный выбор, используем жанр по умолчанию");
                yield new Genre("Другое", "Неопределенный жанр");
            }
        };
    }

    private static void initializeTestData() {
        // тестовые данные
        try {
            Author tolstoy = new Author("Лев Толстой", "Россия");
            Author dostoevsky = new Author("Фёдор Достоевский", "Россия");
            Author pushkin = new Author("Александр Пушкин", "Россия");
            Author king = new Author("Стивен Кинг", "США");
            Author orwell = new Author("Джордж Оруэлл", "Великобритания");

            Genre classic = new Genre("Классика", "Классическая литература");
            Genre novel = new Genre("Роман", "Романы разных эпох");
            Genre dystopia = new Genre("Антиутопия", "Антиутопические романы");
            Genre horror = new Genre("Ужасы", "Ужасы и мистика");

            Book warAndPeace = new Book("Война и мир", tolstoy, classic, 1869);
            Book crimeAndPunishment = new Book("Преступление и наказание", dostoevsky, novel, 1866);
            Book idiot = new Book("Идиот", dostoevsky, novel, 1869);
            Book eugeneOnegin = new Book("Евгений Онегин", pushkin, classic, 1833);
            Book shining = new Book("Сияние", king, horror, 1977);
            Book it = new Book("Оно", king, horror, 1986);
            Book animalFarm = new Book("Скотный двор", orwell, dystopia, 1945);

            library.addBook(warAndPeace);
            library.addBook(crimeAndPunishment);
            library.addBook(idiot);
            library.addBook(eugeneOnegin);
            library.addBook(shining);
            library.addBook(it);
            library.addBook(animalFarm);

            System.out.println("📚 Тестовые данные загружены (7 книг)");

        } catch (DuplicateBookException e) {
            System.err.println("Ошибка загрузки тестовых данных: " + e.getMessage());
        }
    }

    private static Genre selectGenre() {
        System.out.println("\nВыберите жанр:");
        System.out.println("1. Классика");
        System.out.println("2. Фэнтези");
        System.out.println("3. Ужасы");
        System.out.println("4. Роман");
        System.out.println("5. Другой (ввести название)");

        System.out.print("Выберите жанр (1-5): ");
        String choice = scanner.nextLine();

        return switch (choice) {
            case "1" -> new Genre("Классика", "Классическая литература");
            case "2" -> new Genre("Фэнтези", "Фэнтезийная литература");
            case "3" -> new Genre("Ужасы", "Ужасы и мистика");
            case "4" -> new Genre("Роман", "Романы разных эпох");
            case "5" -> {
                System.out.print("Введите название жанра: ");
                String genreName = scanner.nextLine();
                yield new Genre(genreName, "");
            }
            default -> {
                System.err.println("❌ Неверный выбор");
                yield null;
            }
        };
    }
}

