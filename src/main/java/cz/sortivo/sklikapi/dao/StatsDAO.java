/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.sortivo.sklikapi.dao;

import cz.sortivo.sklikapi.Client;
import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.Stats;
import cz.sortivo.sklikapi.bean.Ad;
import cz.sortivo.sklikapi.bean.Campaign;
import cz.sortivo.sklikapi.bean.Group;
import cz.sortivo.sklikapi.bean.Keyword;
import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class StatsDAO {
    private static final String GET_CAMPAIGN_STATS_METHOD_NAME = "campaigns.stats";
    private static final String GET_GROUP_STATS_METHOD_NAME = "groups.stats";
    private static final String GET_KEYWORD_STATS_METHOD_NAME = "keywords.stats";
    private static final String GET_AD_STATS_METHOD_NAME = "ads.stats";
    

    private static final String FIELD_AVG_POSITION =  "avgPosition";
    private static final String FIELD_CLICKS = "clicks";
    private static final String FIELD_IMPRESSIONS = "impressions";
    private static final String FIELD_CONVERSIONS = "conversions";
    private static final String FIELD_TRANSACTIONS = "transactions";
    private static final String FIELD_VALUE = "conversionValue";
    private static final String FIELD_PRICE = "price";

    private Client client;
    
    public StatsDAO(Client client) {
        this.client = client;
    }
    
    /**
     * Get name of api method 
     * @param o - target object, which will be used for getting stats
     * @return - name of suitable api method
     * @throws IllegalArgumentException - if there are no suitable mappings for requested type
     * Typical usage:
     * Keyword keyword = ...
     * Map<String, Object> response = client.sendRequest(getMethodNameByClazz( keyword ), new Object[]{})
     * response: keyword.stats
     */
    private <T extends SKlikObject> String getMethodNameByClazz( Class<T> classType ){
        if (classType.isInstance(Campaign.class)){return GET_CAMPAIGN_STATS_METHOD_NAME;}
        if (classType.isInstance(Group.class)){return GET_GROUP_STATS_METHOD_NAME;}
        if (classType.isInstance(Keyword.class)){return GET_KEYWORD_STATS_METHOD_NAME;}
        if (classType.isInstance(Ad.class)){return GET_AD_STATS_METHOD_NAME;}
        
        throw new IllegalArgumentException("This type is not suitable for available methods");
    }
    
    /**
     * Map response to Stats object
     * @param responseData 
     * @return Stats object filled with data from response
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, Stats> mapStatsObject(Map<String, Object> response){
        Map<Integer, Stats> ret = new HashMap<>();
        
        for (Object object : (Object[]) response.get("report")) {           
            Map<String, Object> responseInfo = (Map<String, Object>) object;
            Integer keywordId = (int) responseInfo.get("keywordId");
            Map<String, Object> responseData = (Map<String, Object>) responseInfo.get("stats");
            Stats stats = new Stats();
            stats.setAvgPosition((double)responseData.get(FIELD_AVG_POSITION));
            stats.setClicks((int)responseData.get(FIELD_CLICKS));
            stats.setConversions((int)responseData.get(FIELD_CONVERSIONS));
            stats.setImpressions((int)responseData.get(FIELD_IMPRESSIONS));
            stats.setMoney((int)responseData.get(FIELD_PRICE));
            stats.setTransactions((int)responseData.get(FIELD_TRANSACTIONS));
            stats.setValue((int)responseData.get(FIELD_VALUE));
            ret.put(keywordId, stats);
        }
        
        
        return ret;
    }
    
    /**
     * Get statistics object for specified SKlikObject 
     * @param sklikObject - FE. keyword, object for which are the stats requested 
     * @param from - date range start 
     * @param to - date range and
     * @return stat object with filled response values
     */
    public <T extends SKlikObject> Map<Integer, Stats> getStats(Class<T> classType, List<Integer> ids, DateTime from, DateTime to)throws SKlikException, InvalidRequestException{
        return mapStatsObject(getResponse(classType, ids, from, to));
    }
   
  
    
    private <T extends SKlikObject> Map<String, Object> getResponse(Class<T> classType, List<Integer> ids, DateTime from, DateTime to)throws SKlikException, InvalidRequestException{
       Map<String, Object> paramsStruct = new HashMap<>();
       paramsStruct.put("dateFrom", from.toDate());
       paramsStruct.put("dateTo", to.toDate());
       paramsStruct.put("granularity", "total");
       return client.sendRequest(getMethodNameByClazz(classType), new Object[]{ids, paramsStruct});
    }
    


}
