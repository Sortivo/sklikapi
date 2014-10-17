package cz.sortivo.sklikapi.bean;
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
    public Integer getClicks() {
        return clicks;
    }
    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }
    public Integer getImpressions() {
        return impressions;
    }
    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }
    public Integer getConversions() {
        return conversions;
    }
    public void setConversions(Integer conversions) {
        this.conversions = conversions;
    }
    public Integer getTransactions() {
        return transactions;
    }
    public void setTransactions(Integer transactions) {
        this.transactions = transactions;
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
    public Integer getMoney() {
        return money;
    }
    public void setMoney(Integer money) {
        this.money = money;
    }
    
    
    
}
