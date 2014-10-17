/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.sortivo.sklikapi.dao;

import cz.sortivo.sklikapi.Client;
import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.bean.Stats;
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
        if (classType.isInstance(new Campaign())){return GET_CAMPAIGN_STATS_METHOD_NAME;}
        if (classType.isInstance(new Group())){return GET_GROUP_STATS_METHOD_NAME;}
        if (classType.isInstance(new Keyword())){return GET_KEYWORD_STATS_METHOD_NAME;}
        if (classType.isInstance(new Ad())){return GET_AD_STATS_METHOD_NAME;}
        
        throw new IllegalArgumentException("This type is not suitable for available methods");
    }
    
    /**
     * Map response to Stats object
     * @param responseData 
     * @return Stats object filled with data from response
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, Stats> mapStatsObject(Map<String, Object> response, String methodName){
        Map<Integer, Stats> ret = new HashMap<>();
        
        for (Object object : (Object[]) response.get("report")) {           
            Map<String, Object> responseInfo = (Map<String, Object>) object;
            Integer id = null;
            switch (methodName){
            case GET_KEYWORD_STATS_METHOD_NAME: 
                id = (int) responseInfo.get("keywordId");
                break;
            case GET_GROUP_STATS_METHOD_NAME:
                id = (int) responseInfo.get("groupId");
                break;
            default:
                throw new RuntimeException("Unknown method name " + methodName);
            }
            
            Object[] statsObjects = (Object[]) responseInfo.get("stats");
            // we want only the first, because there should be only one ( we set granularity to total)            
            Map<String, Object> responseData = (Map<String, Object>) statsObjects[0];
            Stats stats = new Stats();
            stats.setAvgPosition((Double)responseData.get(FIELD_AVG_POSITION));
            stats.setClicks((Integer)responseData.get(FIELD_CLICKS));
            stats.setConversions((Integer)responseData.get(FIELD_CONVERSIONS));
            stats.setImpressions((Integer)responseData.get(FIELD_IMPRESSIONS));
            stats.setMoney((Integer)responseData.get(FIELD_PRICE));
            stats.setTransactions((Integer)responseData.get(FIELD_TRANSACTIONS));
            stats.setValue((Integer)responseData.get(FIELD_VALUE));
            ret.put(id, stats);
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
        String methodName = getMethodNameByClazz(classType);
        return mapStatsObject(getResponse(methodName, ids, from, to), methodName);
    }
   
  
    
    private <T extends SKlikObject> Map<String, Object> getResponse(String methodName, List<Integer> ids, DateTime from, DateTime to)throws SKlikException, InvalidRequestException{
       Map<String, Object> paramsStruct = new HashMap<>();
       paramsStruct.put("dateFrom", from.toDate());
       paramsStruct.put("dateTo", to.toDate());
       paramsStruct.put("granularity", "total");
       return client.sendRequest(methodName, new Object[]{ids, paramsStruct});
    }
    


}
