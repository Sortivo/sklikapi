package cz.sortivo.sklikapi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * http://api.sklik.cz/listAds.html
 * @author Jan Dufek
 */
public class AdDAO {
    
    private static final String LIST_ADS_METHOD_NAME = "ads.list";
    private static final String CREATE_AD_METHOD_NAME = "ad.create";
    private static final String REMOVE_AD_METHOD_NAME = "ad.remove";
    private static final String RESTORE_AD_METHOD_NAME = "ad.remove";
    private static final String UPDATE_METHOD_NAME = "ads.update";
    private static final String  GET_ATTRIBUTES_METHOD_NAME = "ad.getAttributes";
    
    private static final String FIELD_ID= "id";
    private static final String FIELD_CREATIVE_1= "creative1";
    private static final String FIELD_CREATIVE_2 = "creative2";
    private static final String FIELD_CREATIVE_3 = "creative3";
    private static final String FIELD_CLICKTHRU_TEXT = "clickthruText";
    private static final String FIELD_CLICKTHRU_URL = "clickthruUrl";
    private static final String FIELD_REMOVED = "removed";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CREATE_DATE = "createDate";
    private static final String FIELD_GROUP_ID = "groupId";
    private static final String FIELD_PREMISE_MODE = "premiseMode";
    private static final String FIELD_PREMISE_ID = "premiseId";
    
    private Client client;
    
    public AdDAO(Client client) {
        this.client = client;
    }
    
    
    /**
     * 
     * @param ids
     * @param level
     * @param includeDeleted
     * @param userId
     * @return
     * @throws InvalidRequestException
     * @throws SKlikException
     */
    public List<Ad> listAds(List<Integer> ids, EntityType level, boolean includeDeleted, Integer userId) throws InvalidRequestException, SKlikException{
        
        Map<String, Object> restrictionFilter = new LinkedHashMap<>();
        
        String mapIdsKeyName;
        switch(level){
            case CAMPAIGN:
                mapIdsKeyName = "campaignIds";
                break;
            case GROUP:
                mapIdsKeyName = "groupIds";
                break;
            case AD:
                mapIdsKeyName = "adIds";
                break;
            default:
                throw new IllegalAccessError("Unsupported level " + level);
          
        }
        restrictionFilter.put(mapIdsKeyName, ids);
        restrictionFilter.put("includeDeleted", includeDeleted);
        
        Map<String, Object> response = client.sendRequest(LIST_ADS_METHOD_NAME, new Object[]{restrictionFilter}, userId);
        
        List<Ad> ads = new ArrayList<>();
        for (Object object : (Object[])response.get("ads")) {
            ads.add(transformToObject((Map<String, Object>) object));
        }
        
        return ads;
        
    }
    
    public Map<String, Object> pause(List<Ad> ads, Integer userId) throws InvalidRequestException, SKlikException{
        List<Map<String, Object>> adsList = new LinkedList<>();
        Map<String, Object> adMap;
        for (Ad ad : ads) {
            adMap = new LinkedHashMap<>();
            adMap.put("id", ad.getId());
            adMap.put("status", "suspend");
            adsList.add(adMap);
        }
        
        return client.sendRequest(UPDATE_METHOD_NAME, new Object[]{adsList}, userId);
    }
    
    

    private Map<String, Object> transformFromObject(Ad ad){
        Map<String, Object> map = new HashMap<>();
        if (ad.getId() != null)map.put(FIELD_ID, ad.getId());
        if (ad.getCreative1() != null)map.put(FIELD_CREATIVE_1, ad.getCreative1());
        if (ad.getCreative2() != null)map.put(FIELD_CREATIVE_2, ad.getCreative2());
        if (ad.getCreative3() != null)map.put(FIELD_CREATIVE_3, ad.getCreative3());
        if (ad.getClickthruText() != null)map.put(FIELD_CLICKTHRU_TEXT, ad.getClickthruText());
        if (ad.getClickthruUrl() != null)map.put(FIELD_CLICKTHRU_URL, ad.getClickthruUrl());
        map.put(FIELD_REMOVED, ad.isRemoved());
        if (ad.getStatus() != null)map.put(FIELD_STATUS, ad.getStatus().getStatusText());
        if (ad.getCreateDate() != null)map.put(FIELD_CREATE_DATE, ad.getCreateDate());
        if (ad.getGroupId() != null)map.put(FIELD_GROUP_ID, ad.getGroupId());
        if (ad.getPremiseMode() != null)map.put(FIELD_PREMISE_MODE, ad.getPremiseMode());
        if (ad.getPremiseId() != null)map.put(FIELD_PREMISE_ID, ad.getPremiseId());

        return map;
    }

    private Ad transformToObject(Map<String, Object> adResp) throws InvalidRequestException {
        try{
            Ad ad = new Ad();
            if(adResp.get(FIELD_ID) != null) ad.setId((Integer)adResp.get(FIELD_ID));
            if(adResp.get(FIELD_CREATIVE_1) != null)ad.setCreative1((String)adResp.get(FIELD_CREATIVE_1));
            if(adResp.get(FIELD_CREATIVE_2) != null)ad.setCreative2((String)adResp.get(FIELD_CREATIVE_2));
            if(adResp.get(FIELD_CREATIVE_3) != null)ad.setCreative3((String)adResp.get(FIELD_CREATIVE_3));
            if(adResp.get(FIELD_CLICKTHRU_TEXT) != null)ad.setClickthruText((String)adResp.get(FIELD_CLICKTHRU_TEXT));
            if(adResp.get(FIELD_CLICKTHRU_URL) != null)ad.setClickthruUrl((String)adResp.get(FIELD_CLICKTHRU_URL));
            if(adResp.get(FIELD_REMOVED) != null)ad.setRemoved((boolean)adResp.get(FIELD_REMOVED));
            if(adResp.get(FIELD_STATUS) != null)ad.setStatus(Status.getStatus((String)adResp.get(FIELD_STATUS)));
            if(adResp.get(FIELD_CREATE_DATE) != null)ad.setCreateDate(new DateTime(adResp.get(FIELD_CREATE_DATE)));
            if(adResp.get(FIELD_GROUP_ID) != null)ad.setGroupId((Integer)adResp.get(FIELD_GROUP_ID));
            if(adResp.get(FIELD_PREMISE_MODE) != null)ad.setPremiseMode((String)adResp.get(FIELD_PREMISE_MODE));
            if(adResp.get(FIELD_PREMISE_ID) != null)ad.setPremiseId((Integer)adResp.get(FIELD_PREMISE_ID));

            return ad; 
        } catch (NumberFormatException ex){
            throw new InvalidRequestException(ex);
        }
        
    }
    
}
