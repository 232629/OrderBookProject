import java.math.BigDecimal;
import java.util.UUID;

public class Order {

    //valid price from 0.01 to 999999.99
    BigDecimal price;
    //valid price from 1 to 999999
    int quantity;


    public Order(BigDecimal price, int quantity) {

        //validation of price
        this.price = price.setScale(2, BigDecimal.ROUND_DOWN);
        if (price.compareTo(this.price) != 0 )
            throw new IllegalArgumentException();
        if (price.compareTo(new BigDecimal("0")) <= 0 || price.compareTo(new BigDecimal("999999.99")) == 1 )
            throw new IllegalArgumentException();

        //validation of quantity
        if (quantity <= 0 || quantity > 999999)
            throw new IllegalArgumentException();
        else
            this.quantity = quantity;
    }
}
