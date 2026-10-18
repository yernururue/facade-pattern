public class OrderFacadeTest {
    public static void main(String[] args) {
        successfulOrder();
        notEnoughBooks();
        notEnoughMoney();
        shippingFailure();
        invalidInput();
        System.out.println("All facade checks passed");
    }

    private static void successfulOrder() {
        OrderFacade shop = new OrderFacade();
        Customer customer = new Customer("Ali", "Almaty, Abay 10", 10000);

        Order order = shop.placeOrder(1, 2, customer);

        assert shop.getStock(1) == 3 : "Two books must be reserved";
        assert customer.getBalance() == 4000 : "Payment must be deducted";
        assert order.getTotal() == 6000 : "Total must include quantity";
        assert order.getStatus().equals("CONFIRMED");
        assert order.getShippingNumber() != null;

        Order second = shop.placeOrder(1, 3, customerWithMoney());
        assert shop.getStock(1) == 0 : "Exact remaining stock can be ordered";
        assert second.getId() != order.getId();
        assert !second.getShippingNumber().equals(order.getShippingNumber());
    }

    private static void notEnoughBooks() {
        OrderFacade shop = new OrderFacade();
        Customer customer = customerWithMoney();
        try {
            shop.placeOrder(1, 6, customer);
            throw new AssertionError("An unavailable quantity was accepted");
        } catch (IllegalStateException expected) {
            assert shop.getStock(1) == 5 : "Stock must stay unchanged";
            assert customer.getBalance() == 10000 : "Payment must not happen";
        }
    }

    private static void notEnoughMoney() {
        OrderFacade shop = new OrderFacade();
        Customer customer = new Customer("Ali", "Almaty, Abay 10", 1000);
        try {
            shop.placeOrder(1, 2, customer);
            throw new AssertionError("An unpaid order was accepted");
        } catch (IllegalStateException expected) {
            assert shop.getStock(1) == 5 : "Reserved books must be released";
            assert customer.getBalance() == 1000 : "No refund without payment";
        }
        shop.placeOrder(1, 5, new Customer("Dana", "Astana, Abay 5", 15000));
        assert shop.getStock(1) == 0 : "Released books must be available again";
    }

    private static void shippingFailure() {
        OrderFacade shop = new OrderFacade();
        Customer customer = new Customer("Ali", " ", 10000);
        try {
            shop.placeOrder(1, 2, customer);
            throw new AssertionError("An order without an address was accepted");
        } catch (IllegalArgumentException expected) {
            assert shop.getStock(1) == 5 : "Reserved books must be released";
            assert customer.getBalance() == 10000 : "Payment must be refunded";
        }
        Order next = shop.placeOrder(1, 1, customerWithMoney());
        assert next.getShippingNumber().equals("SHIP-1")
                : "A failed shipment must not consume a shipping number";

        Customer nullAddress = new Customer("Dana", null, 10000);
        try {
            shop.placeOrder(2, 1, nullAddress);
            throw new AssertionError("A null address was accepted");
        } catch (IllegalArgumentException expected) {
            assert shop.getStock(2) == 3;
            assert nullAddress.getBalance() == 10000;
        }
    }

    private static void invalidInput() {
        OrderFacade shop = new OrderFacade();
        Customer customer = customerWithMoney();
        for (int quantity : new int[]{0, -1}) {
            try {
                shop.placeOrder(1, quantity, customer);
                throw new AssertionError("An invalid quantity was accepted");
            } catch (IllegalArgumentException expected) {
                assert shop.getStock(1) == 5;
                assert customer.getBalance() == 10000;
            }
        }
        try {
            shop.placeOrder(99, 1, customer);
            throw new AssertionError("An unknown book was accepted");
        } catch (IllegalArgumentException expected) {
            assert customer.getBalance() == 10000;
        }
        try {
            shop.placeOrder(1, 1, null);
            throw new AssertionError("A missing customer was accepted");
        } catch (IllegalArgumentException expected) {
            assert shop.getStock(1) == 5;
        }
    }

    private static Customer customerWithMoney() {
        return new Customer("Dana", "Astana, Abay 5", 10000);
    }
}
