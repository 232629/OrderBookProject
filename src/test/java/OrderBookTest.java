import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@ExtendWith(MyTestWatcher.class)
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


    @Test
    void checkPriceOrderMarketData() {

        final int quantity = 1;
        String small = "0.01";
        String medium = "10.01";
        String large = "999999.99";

        ob.addAsk(new BigDecimal(large), quantity);
        ob.addAsk(new BigDecimal(small), quantity);
        ob.addAsk(new BigDecimal(medium), quantity);
        ob.addBid(new BigDecimal(large), quantity);
        ob.addBid(new BigDecimal(small), quantity);
        ob.addBid(new BigDecimal(medium), quantity);


        Gson gson = new Gson();
        Map mapMarketData = gson.fromJson(ob.getMarketData(), Map.class);

        Assert.assertEquals(2, mapMarketData.size());
        Assert.assertEquals(3, ((ArrayList) mapMarketData.get("asks")).size());
        Assert.assertEquals(3, ((ArrayList) mapMarketData.get("bids")).size());

        Assert.assertEquals(small, gson.fromJson(((ArrayList) mapMarketData.get("asks")).get(0).toString(), Map.class).get("price").toString());
        Assert.assertEquals(medium, gson.fromJson(((ArrayList) mapMarketData.get("asks")).get(1).toString(), Map.class).get("price").toString());
        Assert.assertEquals(large, gson.fromJson(((ArrayList) mapMarketData.get("asks")).get(2).toString(), Map.class).get("price").toString());
        Assert.assertEquals(small, gson.fromJson(((ArrayList) mapMarketData.get("bids")).get(0).toString(), Map.class).get("price").toString());
        Assert.assertEquals(medium, gson.fromJson(((ArrayList) mapMarketData.get("bids")).get(1).toString(), Map.class).get("price").toString());
        Assert.assertEquals(large, gson.fromJson(((ArrayList) mapMarketData.get("bids")).get(2).toString(), Map.class).get("price").toString());

    }

    @Test
    void checkQuantityMarketData() {

        final int quantity = 1;
        String price = "0.01";

        ob.addAsk(new BigDecimal(price), quantity);
        ob.addBid(new BigDecimal(price), quantity);

        Assert.assertEquals("{\n" +
                "  \"asks\": [\n" +
                "    {\n" +
                "      \"quantity\": " + quantity + ",\n" +
                "      \"price\": 0.01\n" +
                "    }\n" +
                "  ],\n" +
                "  \"bids\": [\n" +
                "    {\n" +
                "      \"quantity\": " + quantity + ",\n" +
                "      \"price\": 0.01\n" +
                "    }\n" +
                "  ]\n" +
                "}",
                ob.getMarketData());
    }

    @Test
    void getEmptyMarketData() {
        Gson gson = new Gson();
        Map mapMarketData = gson.fromJson(ob.getMarketData(), Map.class);

        Assert.assertEquals(2, mapMarketData.size());
        Assert.assertEquals(0, gson.fromJson(mapMarketData.get("asks").toString(), Map.class).size());
        Assert.assertEquals(0, gson.fromJson(mapMarketData.get("bids").toString(), Map.class).size());
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