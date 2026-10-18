import java.util.List;

public class OrderFacade {
    private final InventoryService inventory = new InventoryService();
    private final PaymentProcessor payment = new PaymentProcessor();
    private final ShippingService shipping = new ShippingService();
    private final NotificationService notification = new NotificationService();
    private int nextOrderId = 1;

    public List<Book> getBooks() {
        return inventory.getBooks();
    }

    public int getStock(int bookId) {
        return inventory.getStock(bookId);
    }

    public Order placeOrder(int bookId, int quantity, Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }
        inventory.checkStock(bookId, quantity);
        Book book = inventory.getBook(bookId);
        Order order = new Order(nextOrderId++, book, customer, quantity);

        inventory.reserve(bookId, quantity);
        boolean paid = false;
        String shippingNumber;
        try {
            payment.pay(customer, order.getTotal());
            paid = true;
            shippingNumber = shipping.createShipment(order);
        } catch (IllegalArgumentException | IllegalStateException e) {
            if (paid) {
                payment.refund(customer, order.getTotal());
            }
            inventory.release(bookId, quantity);
            order.cancel();
            throw e;
        }

        order.confirm(shippingNumber);
        notification.sendConfirmation(order);
        return order;
    }
}
