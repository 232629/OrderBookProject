import java.math.BigDecimal;
import java.util.UUID;

public class Order {

    String id;
    double price;
    double quantity;
    BigDecimal pr;


    public Order(double price, double quantity) {
        this.id = UUID.randomUUID().toString();
        this.price = price;
        this.quantity = quantity;
    }
}
