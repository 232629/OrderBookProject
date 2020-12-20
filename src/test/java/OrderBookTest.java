import org.junit.Assert;
import org.junit.jupiter.api.*;

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
        ob.addAsk(6.50,1.11);
        ob.addAsk(8,1.41);
        ob.addAsk(1,1.61);
        ob.addAsk(5.5,1.61);
        ob.addAsk(1000,1.61);
        ob.addBid(7,3.33);
        ob.addBid(2,3.33);
        ob.addBid(9,3.33);
        ob.addBid(1111,3.33);
        ob.addBid(1111,3.33);
        ob.addBid(1111,3.33);
        ob.addBid(1111,3.33);


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