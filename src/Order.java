public class Order {
    private final int id;
    private final Book book;
    private final Customer customer;
    private final int quantity;
    private final int total;
    private String status = "NEW";
    private String shippingNumber;

    public Order(int id, Book book, Customer customer, int quantity) {
        this.id = id;
        this.book = book;
        this.customer = customer;
        this.quantity = quantity;
        this.total = book.getPrice() * quantity;
    }

    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getShippingNumber() {
        return shippingNumber;
    }

    public void confirm(String shippingNumber) {
        this.shippingNumber = shippingNumber;
        status = "CONFIRMED";
    }

    public void cancel() {
        status = "CANCELLED";
    }
}
