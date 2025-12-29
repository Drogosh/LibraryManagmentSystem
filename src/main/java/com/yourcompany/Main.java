import com.yourcompany.Author;
import com.yourcompany.Book;
import com.yourcompany.Genre;
import com.yourcompany.Library;

void main() {
    Library library = new Library();
    Author author = new Author("test", "test");
    Genre genre = new Genre("test", "test");
    Book book = new Book("test", author, genre, 2022);
    Book book1 = new Book("test1", author, genre, 2023);
    library.addBook(book);
    library.addBook(book1);
    System.out.println(library.toString());


}
