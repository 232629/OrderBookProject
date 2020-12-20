import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.*;


public class OrderBook implements IOrderBook {

    HashMap<UUID, Order> asks = new HashMap<UUID, Order>();
    HashMap<UUID, Order> bids = new HashMap<UUID, Order>();

    public UUID addAsk (BigDecimal price, int quantity) {
        return addOrder(true, price, quantity);
    }

    public UUID addBid (BigDecimal price, int quantity) {
        return addOrder(false, price, quantity);
    }

    public boolean cancelOrder (UUID id) {
        if (asks.containsKey(id)) {
            asks.remove(id);
            return true;
        }
        else if (bids.containsKey(id)) {
            bids.remove(id);
            return true;
        }

        return false;
    }

    public Order getOrder (UUID id) {
        if (asks.containsKey(id))
            return asks.get(id);
        else if (bids.containsKey(id))
            return bids.get(id);

        return null;
    }

    public String getMarketData () {

        JSONObject jsonOrderBook = new JSONObject();
        jsonOrderBook.put("asks", formatData(asks));
        jsonOrderBook.put("bids", formatData(bids));
        return jsonOrderBook.toJSONString();
    }

    private UUID addOrder (boolean ask, BigDecimal price, int quantity) {
        UUID id = UUID.randomUUID();
        if (ask)
            asks.put(id, new Order(price, quantity));
        else
            bids.put(id, new Order(price, quantity));
        return id;
    }

    private JSONArray formatData(HashMap<UUID, Order> orders) {

        List<Order> listOrders = new ArrayList<>(orders.values());
        listOrders.sort(Comparator.comparing(order -> order.price));

        JSONArray jaOrders = new JSONArray();
        for (Order a: listOrders) {
            JSONObject jo = new JSONObject();
            //jo.put("id", a.id);
            jo.put("price", a.price);
            jo.put("quantity", a.quantity);

            jaOrders.add(jo);
        }

        return jaOrders;
    }


}
