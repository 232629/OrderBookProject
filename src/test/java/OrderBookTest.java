import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

class OrderBookTest {

    private OrderBook ob;

    @BeforeAll
    static void setUpBeforeAll() {
    }

    @BeforeEach
    void setUpBeforeEach(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
        ob = new OrderBook();
    }
    //----------

    @Test
    void doubleAddSameAskOrder() {
        final BigDecimal price = new BigDecimal("1.11");
        final int quantity = 100;

        UUID id1 = ob.addAsk(price, quantity);
        UUID id2 = ob.addAsk(price, quantity);
        Assert.assertNotEquals(id1, id2);
        Assert.assertNotNull(ob.getOrder(id1));
        Assert.assertNotNull(ob.getOrder(id2));
    }
    @Test
    void doubleAddSameBidOrder() {
        final BigDecimal price = new BigDecimal("1.11");
        final int quantity = 100;

        UUID id1 = ob.addBid(price, quantity);
        UUID id2 = ob.addBid(price, quantity);
        Assert.assertNotEquals(id1, id2);
        Assert.assertNotNull(ob.getOrder(id1));
        Assert.assertNotNull(ob.getOrder(id2));
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 555555, 999999})
    void addValidQuantityAsk(int quantity) {
        Assert.assertEquals(quantity, ob.getOrder(ob.addAsk(new BigDecimal("1"), quantity)).quantity);
    }
    @ParameterizedTest
    @ValueSource(ints = {1, 555555, 999999})
    void addValidQuantityBid(int quantity) {
        Assert.assertEquals(quantity, ob.getOrder(ob.addBid(new BigDecimal("1"), quantity)).quantity);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1000000})
    void addNotValidQuantityAsk(int quantity) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ob.addAsk(new BigDecimal("1"), quantity);
        });
    }
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1000000})
    void addNotValidQuantityBid(int quantity) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ob.addBid(new BigDecimal("1"), quantity);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {".3", "0.01", "555555", "999999.99", "00000.05000000000000", "0.30", "4.5"})
    void addValidPriceAsk(String price) {
        Assert.assertEquals(new BigDecimal(price).setScale(2, BigDecimal.ROUND_DOWN), ob.getOrder(ob.addAsk(new BigDecimal(price), 555)).price);
    }
    @ParameterizedTest
    @ValueSource(strings = {".3", "0.01", "555555", "999999.99", "00000.05000000000000", "0.30", "4.5"})
    void addValidPriceBid(String price) {
        Assert.assertEquals(new BigDecimal(price).setScale(2, BigDecimal.ROUND_DOWN), ob.getOrder(ob.addBid(new BigDecimal(price), 555)).price);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.0", "-1", "1000000", "0.001" })
    void addllegalArgumentExceptionPriceAsk(String price) {
       Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ob.addAsk(new BigDecimal(price), 666);
        });
    }
    @ParameterizedTest
    @ValueSource(strings = {"0", "0.0", "-1", "1000000", "0.001" })
    void addllegalArgumentExceptionPriceBid(String price) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ob.addBid(new BigDecimal(price), 666);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"0,0", "one", "", "null", "." })
    void addFormatExceptionPriceAsk(String price) {
        Assertions.assertThrows(NumberFormatException.class, () -> {
            ob.addAsk(new BigDecimal(price), 666);
        });
    }
    @ParameterizedTest
    @ValueSource(strings = {"0,0", "one", "", "null", "." })
    void addFormatExceptionPriceBid(String price) {
        Assertions.assertThrows(NumberFormatException.class, () -> {
            ob.addBid(new BigDecimal(price), 666);
        });
    }


    @Test
    void cancelNotExistOrder() {
        Assert.assertNull(ob.getOrder(UUID.randomUUID()));
    }

    @Test
    void cancelNotExistOrderNull() {
        Assert.assertNull(ob.getOrder(null));
    }

    @Test
    void cancelExistAskOrder() {
        UUID id = ob.addAsk(new BigDecimal("0.01"), 999);
        Assert.assertEquals(id, ob.cancelOrder(id));
    }
    @Test
    void cancelExistBidOrder() {
        UUID id = ob.addBid(new BigDecimal("0.01"), 999);
        Assert.assertEquals(id, ob.cancelOrder(id));
    }


    @Test
    void getNotExistOrder() {
        Assert.assertNull(ob.getOrder(UUID.randomUUID()));
    }

    @Test
    void getNotExistOrderNull() {
        Assert.assertNull(ob.getOrder(null));
    }

    @Test
    void getExistAskOrder() {
        final BigDecimal price = new BigDecimal("1.11");
        final int quantity = 100;
        UUID id = ob.addAsk(price, quantity);
        Assert.assertEquals(price ,ob.getOrder(id).price);
        Assert.assertEquals(quantity, ob.getOrder(id).quantity);
    }
    @Test
    void getExistBidOrder() {
        final BigDecimal price = new BigDecimal("1.11");
        final int quantity = 100;
        UUID id = ob.addBid(price, quantity);
        Assert.assertEquals(price ,ob.getOrder(id).price);
        Assert.assertEquals(quantity, ob.getOrder(id).quantity);
    }


    //todo
    @Test
    void getEmptyMarketData() {

        Gson gson = new Gson();
        Map mapMarketData = gson.fromJson(ob.getMarketData(), Map.class);

        Assert.assertEquals(2, mapMarketData.size());

        Assert.assertEquals("{\n" +
                "  \"asks\": [],\n" +
                "  \"bids\": []\n" +
                "}", ob.getMarketData());
        
    }

    //-----
    @AfterEach
    void tearDownAfterEach(TestInfo testInfo) {
        System.out.println(ob.getMarketData());

    }

    @AfterAll
    static void tearDownAfterAll() {
    }
}