public interface IOrderBook {
    String addAsk(double price, double quantity );
    String addBid(String price, double quantity );
    void cancelOrder (String id);
    String getOrder (String id);
    String getMarketData ();
}
