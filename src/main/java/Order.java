import java.math.BigDecimal;
import java.util.UUID;

public class Order {

    UUID id;
    BigDecimal price;
    int quantity;


    public Order(BigDecimal price, int quantity) {
        this.id = UUID.randomUUID();

        this.price = price.setScale(2, BigDecimal.ROUND_DOWN);
        if (price.compareTo(this.price) != 0 )
            throw new IllegalArgumentException();
        if (price.compareTo(new BigDecimal("0")) <= 0 || price.compareTo(new BigDecimal("999999.99")) == 1 )
            throw new IllegalArgumentException();

        if (quantity <= 0 || quantity > 999999)
            throw new IllegalArgumentException();
        else
            this.quantity = quantity;
    }
}
