package cz.sortivo.sklikapi;
/**
 * 
 * @author michal
 *
 */
public class Stats {

    private Double  avgPosition;     //Average ad position
    private Integer clicks;              //Click count
    private Integer impressions;         //View count
    private Integer conversions;         //Conversion count
    private Integer transactions;        //Transaction count
    private Integer value;               //Value of all conversions (in halers)
    private Integer money;               //Cost of all clicks (in halers)
    
    public double getAvgPosition() {
        return avgPosition;
    }
    public void setAvgPosition(double avgPosition) {
        this.avgPosition = avgPosition;
    }
    public int getClicks() {
        return clicks;
    }
    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
    public int getImpressions() {
        return impressions;
    }
    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }
    public int getConversions() {
        return conversions;
    }
    public void setConversions(int conversions) {
        this.conversions = conversions;
    }
    public int getTransactions() {
        return transactions;
    }
    public void setTransactions(int transactions) {
        this.transactions = transactions;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    
    
    
}
