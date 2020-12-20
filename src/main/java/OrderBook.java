import com.sun.org.apache.xerces.internal.xs.StringList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;


public class OrderBook {




    HashMap<UUID, Order> asks = new HashMap<UUID, Order>();
    HashMap<UUID, Order> bids = new HashMap<UUID, Order>();


    public OrderBook() {

    }

    public String addOrder () {

        return "";
    }

    public UUID addAsk (double price, double quantity) {
        UUID id = UUID.randomUUID();
        asks.put(id, new Order(price, quantity));
        return id;
    }

    public UUID addBid (double price, double quantity) {
        UUID id = UUID.randomUUID();
        bids.put(id, new Order(price, quantity));
        return id;
    }

    public boolean cancelOrder (String id) {
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


    private JSONArray formatData(HashMap<UUID, Order> orders) {

        //List<Order> listOrders = orders.values().stream().collect(Collectors.toList());
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
