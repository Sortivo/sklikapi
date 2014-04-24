package cz.sortivo.sklikapi;

import java.util.Map;

import org.joda.time.DateTime;

import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;
import java.util.HashMap;

/**
 * 
 * @author Michal Ličko
 *
 */
public class StatsDAO {
    private static final String GET_CAMPAIGN_STATS_METHOD_NAME = "campaign.stats";
    private static final String GET_GROUP_STATS_METHOD_NAME = "group.stats";
    private static final String GET_KEYWORD_STATS_METHOD_NAME = "keyword.stats";
    private static final String GET_KEYWORDS_STATS_METHOD_NAME = "keywords.stats";
    private static final String GET_AD_STATS_METHOD_NAME = "ad.stats";
    
    private static final String FIELD_STATS = "stats";
    private static final String FIELD_CONTEXT = "context";
    private static final String FIELD_FULLTEXT = "fulltext";
    

    private static final String FIELD_AVG_POSITION =  "avgPosition";
    private static final String FIELD_CLICKS = "clicks";
    private static final String FIELD_IMPRESSIONS = "impressions";
    private static final String FIELD_CONVERSIONS = "conversions";
    private static final String FIELD_TRANSACTIONS = "transactions";
    private static final String FIELD_VALUE = "value";
    private static final String FIELD_MONEY = "money";
    
    private static final String FIELD_KEYWORDS_STATS = "keywordStats";
    private static final String FIELD_KEYWORDS_STATS_ID = "keywordId";

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
    private String getMethodNameByClazz( SKlikObject o){
        
        if (o instanceof Campaign){return GET_CAMPAIGN_STATS_METHOD_NAME;}
        if (o instanceof Group){return GET_GROUP_STATS_METHOD_NAME;}
        if (o instanceof Keyword){return GET_KEYWORD_STATS_METHOD_NAME;}
        if (o instanceof Ad){return GET_AD_STATS_METHOD_NAME;}
        
        throw new IllegalArgumentException("This type is not suitable for available methods");
    }
    
    private String getMultipleMethodNameByClazz( Class object){

        if (object == Keyword.class){
            return GET_KEYWORDS_STATS_METHOD_NAME;
        }        
        throw new IllegalArgumentException("This type is not suitable for available methods");
    }

    
    /**
     * Map response to Stats object
     * @param responseData 
     * @return Stats object filled with data from response
     */
    @SuppressWarnings("unchecked")
    private Stats mapStatsObject(Map<String, Object> response, String statType){
        Map<String, Object> responseData = (Map<String, Object>) response.get(statType);
        Stats stats = new Stats();
        stats.setAvgPosition((double)responseData.get(FIELD_AVG_POSITION));
        stats.setClicks((int)responseData.get(FIELD_CLICKS));
        stats.setConversions((int)responseData.get(FIELD_CONVERSIONS));
        stats.setImpressions((int)responseData.get(FIELD_IMPRESSIONS));
        stats.setMoney((int)responseData.get(FIELD_MONEY));
        stats.setTransactions((int)responseData.get(FIELD_TRANSACTIONS));
        stats.setValue((int)responseData.get(FIELD_VALUE));
        return stats;
    }

    
    /**
     * Get statistics object for specified SKlikObject 
     * @param sklikObject - FE. keyword, object for which are the stats requested 
     * @param from - date range start 
     * @param to - date range and
     * @return stat object with filled response values
     */
    public Stats getStats(SKlikObject  sKlikObject, DateTime from, DateTime to)throws InvalidRequestException, SKlikException{
        return mapStatsObject(getResponse(sKlikObject, from, to), FIELD_STATS);
    }
    
    /**
     * Get multiple statistics
     * @param sKlikObjectClass
     * @param sKlikObjectsIds
     * @param from
     * @param to
     * @return Map from sklikObjectId to stats
     * @throws InvalidRequestException
     * @throws SKlikException 
     */
    public Map<Integer, Stats> getMultipleOfStats(Class sKlikObjectClass, Integer[] sKlikObjectsIds, DateTime from, DateTime to)throws InvalidRequestException, SKlikException{
        Map<Integer, Stats> stats = new HashMap<>();
        Map<String, Object> response = getMultipleResponse(sKlikObjectClass, sKlikObjectsIds, from, to);
        String fieldCollection;
        String fieldEntityId;
        if (sKlikObjectClass == Keyword.class){
            fieldCollection = FIELD_KEYWORDS_STATS;
            fieldEntityId = FIELD_KEYWORDS_STATS_ID;
        } else {
            throw new IllegalArgumentException("This type is not suitable for available methods");
        }
        
        Map<String, Object>[] collection = (Map<String, Object>[]) response.get(fieldCollection);
        for (Map<String, Object> stat : collection) {
            Integer id = (Integer) stat.get(fieldEntityId);
            stats.put(id, mapStatsObject(stat, FIELD_STATS));
        }
        return stats;
    }
    
    /**
     * Get context statistics object for specified SKlikObject 
     * @param sklikObject - FE. keyword, object for which are the stats requested 
     * @param from - date range start 
     * @param to - date range and
     * @return stat object with filled response values
     */
    public Stats getContext(SKlikObject  sKlikObject, DateTime from, DateTime to)throws InvalidRequestException, SKlikException{
        return mapStatsObject(getResponse(sKlikObject, from, to), FIELD_CONTEXT);
    }
    
    /**
     * Get fulltext statistics object for specified SKlikObject 
     * @param sklikObject - FE. keyword, object for which are the stats requested 
     * @param from - date range start 
     * @param to - date range and
     * @return stat object with filled response values
     */
    public Stats getFulltext(SKlikObject  sKlikObject, DateTime from, DateTime to)throws InvalidRequestException, SKlikException{
        return mapStatsObject(getResponse(sKlikObject, from, to), FIELD_FULLTEXT);
    }
    
    private Map<String, Object> getMultipleResponse(Class sKlikObjectClass, Integer[] sKlikObjectsIds, DateTime from, DateTime to)throws InvalidRequestException, SKlikException{
       return client.sendRequest(getMultipleMethodNameByClazz(sKlikObjectClass), new Object[]{sKlikObjectsIds, from.toDate(), to.toDate()});
    }
    
    private Map<String, Object> getResponse(SKlikObject  sKlikObject, DateTime from, DateTime to)throws InvalidRequestException, SKlikException{
       return client.sendRequest(getMethodNameByClazz(sKlikObject), new Object[]{sKlikObject.getId(), from.toDate(), to.toDate()});
    }
    


}
