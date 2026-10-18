public class PaymentProcessor {
    public void pay(Customer customer, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        if (customer.getBalance() < amount) {
            throw new IllegalStateException("Not enough money");
        }
        customer.setBalance(customer.getBalance() - amount);
    }

    public void refund(Customer customer, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Refund amount must be positive");
        }
        customer.setBalance(customer.getBalance() + amount);
    }
}
