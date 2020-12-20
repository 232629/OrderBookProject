import java.math.BigDecimal;
import java.util.UUID;

public interface IOrderBook {
    UUID addAsk(BigDecimal price, int quantity);
    UUID addBid(BigDecimal price, int quantity);
    UUID cancelOrder (UUID id);
    Order getOrder (UUID id);
    String getMarketData ();
}
