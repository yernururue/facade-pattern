public class ShippingService {
    private int nextShippingNumber = 1;

    public String createShipment(Order order) {
        String address = order.getCustomer().getAddress();
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Delivery address is required");
        }
        return "SHIP-" + nextShippingNumber++;
    }
}
