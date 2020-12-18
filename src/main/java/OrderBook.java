import org.json.simple.JSONObject;

public class OrderBook implements IOrderBook{

    JSONObject jsonOrderBook;

    public OrderBook() {
        jsonOrderBook = new JSONObject();

    }

    public String addOrder () {

        return "";
    }

    public String addAsk (String price, double quantity) {
        return null;
    }

    public String addBid (String price, double quantity) {
        return null;
    }

    public void cancelOrder (String id) {

    }

    public String getOrder (String id) {

        return "";
    }

    public String getMarketData () {

        return "";
    }

}
