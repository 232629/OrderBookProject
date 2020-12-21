import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.*;


public class OrderBook implements IOrderBook {

    //List of asks
    private HashMap<UUID, Order> asks;
    //List of bids
    private HashMap<UUID, Order> bids;

    public OrderBook() {
        asks = new HashMap<UUID, Order>();
        bids = new HashMap<UUID, Order>();
    }

    public UUID addAsk (BigDecimal price, int quantity) {
        return addOrder(OrderType.ASK, price, quantity);
    }

    public UUID addBid (BigDecimal price, int quantity) {
        return addOrder(OrderType.BID, price, quantity);
    }

    private UUID addOrder (OrderType orderType, BigDecimal price, int quantity) {
        UUID id = UUID.randomUUID();
        switch(orderType) {
            case ASK:
                //add new ask
                asks.put(id, new Order(price, quantity));
                break;
            case BID:
                //add new bid
                bids.put(id, new Order(price, quantity));
                break;
        }
        return id;
    }

    public UUID cancelOrder (UUID id) {
        if (asks.containsKey(id)) {
            asks.remove(id);
            return id;
        }
        else if (bids.containsKey(id)) {
            bids.remove(id);
            return id;
        }
        //if order is not exist
        return null;
    }

    public Order getOrder (UUID id) {
        if (asks.containsKey(id))
            return asks.get(id);
        else if (bids.containsKey(id))
            return bids.get(id);
        //if order is not exist
        return null;
    }

    public String getMarketData () {
        JSONObject jsonOrderBook = new JSONObject();
        jsonOrderBook.put("asks", formatData(asks));
        jsonOrderBook.put("bids", formatData(bids));

        //return format json of asks and bids
        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonOrderBook);
    }


    private JSONArray formatData(HashMap<UUID, Order> orders) {

        //sort asks and bids
        List<Order> listOrders = new ArrayList<>(orders.values());
        listOrders.sort(Comparator.comparing(order -> order.price));

        //build json of asks and bids
        JSONArray jaOrders = new JSONArray();
        for (Order a: listOrders) {
            JSONObject jo = new JSONObject();
            jo.put("price", a.price);
            jo.put("quantity", a.quantity);

            jaOrders.add(jo);
        }

        return jaOrders;
    }


}
