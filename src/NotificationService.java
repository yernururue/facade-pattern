public class NotificationService {
    public void sendConfirmation(Order order) {
        if (!order.getStatus().equals("CONFIRMED")) {
            throw new IllegalStateException("Only confirmed orders can be sent");
        }
        String message = "Order #" + order.getId() + " confirmed for "
                + order.getCustomer().getName() + ": "
                + order.getBook().getTitle() + " x " + order.getQuantity()
                + ", total: " + order.getTotal() + " KZT"
                + ", shipping: " + order.getShippingNumber();
        System.out.println(message);
    }
}
