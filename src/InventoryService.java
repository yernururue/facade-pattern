import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
    private final List<Book> books = new ArrayList<>();
    private final Map<Integer, Integer> stock = new HashMap<>();

    public InventoryService() {
        addBook(new Book(1, "Clean Code", 3000), 5);
        addBook(new Book(2, "The Hobbit", 2500), 3);
        addBook(new Book(3, "Java Basics", 2000), 4);
    }

    private void addBook(Book book, int quantity) {
        books.add(book);
        stock.put(book.getId(), quantity);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public Book getBook(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                return book;
            }
        }
        throw new IllegalArgumentException("Book not found: " + bookId);
    }

    public int getStock(int bookId) {
        getBook(bookId);
        return stock.get(bookId);
    }

    public void checkStock(int bookId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (getStock(bookId) < quantity) {
            throw new IllegalStateException("Not enough books in stock");
        }
    }

    public void reserve(int bookId, int quantity) {
        checkStock(bookId, quantity);
        stock.put(bookId, getStock(bookId) - quantity);
    }

    public void release(int bookId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        stock.put(bookId, getStock(bookId) + quantity);
    }
}
