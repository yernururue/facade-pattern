public class Main {
    public static void main(String[] args) {
        OrderFacade shop = new OrderFacade();
        System.out.println("Book catalog (prices in KZT):");
        for (Book book : shop.getBooks()) {
            System.out.println(book.getId() + ". " + book.getTitle()
                    + " - " + book.getPrice() + ", stock: " + shop.getStock(book.getId()));
        }

        Book book = shop.getBooks().get(0);

        System.out.println("\n1. Successful order");
        Customer ali = new Customer("Ali", "Almaty, Abay 10", 10000);
        runCase(shop, book, ali, 2);

        System.out.println("\n2. Not enough books");
        Customer dana = new Customer("Dana", "Astana, Abay 5", 10000);
        runCase(shop, book, dana, 10);

        System.out.println("\n3. Not enough money: release reserved books");
        Customer asel = new Customer("Asel", "Almaty, Satpayev 20", 1000);
        runCase(shop, book, asel, 1);

        System.out.println("\n4. Shipping failure: refund and release reserved books");
        Customer arman = new Customer("Arman", "", 10000);
        runCase(shop, book, arman, 1);
    }

    private static void runCase(OrderFacade shop, Book book, Customer customer, int quantity) {
        System.out.println("Request: " + book.getTitle() + " x " + quantity);
        printState("Before", shop, book, customer);
        try {
            Order order = shop.placeOrder(book.getId(), quantity, customer);
            System.out.println("Result: " + order.getStatus());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Order failed: " + e.getMessage());
        }
        printState("After", shop, book, customer);
    }

    private static void printState(String label, OrderFacade shop, Book book, Customer customer) {
        System.out.println(label + ": stock = " + shop.getStock(book.getId())
                + ", balance = " + customer.getBalance() + " KZT");
    }
}
