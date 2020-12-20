import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

class OrderBookTest {

    @BeforeAll
    static void setUpBeforeAll() {
    }

    @BeforeEach
    void setUpBeforeEach() {
    }

    @Test
    void addAsk() {
        OrderBook ob = new OrderBook();
        ob.addAsk(new BigDecimal("0.1"),99999);
        ob.addAsk(new BigDecimal("6.5345634353450"),555);
        ob.addAsk(new BigDecimal("11111111"),00000000000035);
        ob.addAsk(new BigDecimal("166.50"),01);
        ob.addAsk(new BigDecimal("-1000"),44445);
        ob.addBid(new BigDecimal("777777777777777777777777777777777.777777777777777777777777777777777777777777777"),4525);
        ob.addBid(new BigDecimal(6.50),45254);
        ob.addBid(new BigDecimal(6.50),020054);
        ob.addBid(new BigDecimal(6.50),36345);
        ob.addBid(new BigDecimal(6.50),45345);



        System.out.println(ob.getMarketData());

    }

    @Test
    void getMarketDataTest() {

    }

    @AfterEach
    void tearDownAfterEach() {
    }

    @AfterAll
    static void tearDownAfterAll() {
    }


}