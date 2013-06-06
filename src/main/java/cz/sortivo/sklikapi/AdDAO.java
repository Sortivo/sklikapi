package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalideRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

/**
 * http://api.sklik.cz/listAds.html
 * @author Jan Dufek
 */
public class AdDAO {
    
    private static final String LIST_ADS_METHOD_NAME = "listAds";
    private static final String CREATE_AD_METHOD_NAME = "ad.create";
    private static final String REMOVE_AD_METHOD_NAME = "ad.remove";
    
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
    
    public List<Ad> listAds(int groupId) throws InvalideRequestException, SKlikException{
        Map<String, Object> response = client.sendRequest(LIST_ADS_METHOD_NAME, new Object[]{groupId});
        List<Ad> ads = new ArrayList<>();
        Object[] adsResp = (Object[]) response.get("ads");
        
        for (Object adResp : adsResp) {
            ads.add(transformToObject((Map<String, Object>)adResp));
        }
        return ads;
    }
    
   
    public boolean create(int groupId, Ad ad) throws InvalideRequestException, SKlikException{
        client.sendRequest(CREATE_AD_METHOD_NAME, new Object[]{groupId, transformFromObject(ad)});
        return true;
    }
    
    public boolean remove(int adId) throws InvalideRequestException, SKlikException{
        client.sendRequest(REMOVE_AD_METHOD_NAME, new Object[]{adId});
        return true;
    }
    
    private Map<String, Object> transformFromObject(Ad ad){
        Map<String, Object> map = new HashMap<>();
        map.put(FIELD_ID, ad.getId());
        map.put(FIELD_CREATIVE_1, ad.getCreative1());
        map.put(FIELD_CREATIVE_2, ad.getCreative2());
        map.put(FIELD_CREATIVE_3, ad.getCreative3());
        map.put(FIELD_CLICKTHRU_TEXT, ad.getClickthruText());
        map.put(FIELD_CLICKTHRU_URL, ad.getClickthruUrl());
        map.put(FIELD_REMOVED, ad.isRemoved());
        map.put(FIELD_STATUS, ad.getStatus());
        map.put(FIELD_CREATE_DATE, ad.getCreateDate());
        map.put(FIELD_GROUP_ID, ad.getGroupId());
        map.put(FIELD_PREMISE_MODE, ad.getPremiseMode());
        map.put(FIELD_PREMISE_ID, ad.getPremiseId());

        return map;
    }

    private Ad transformToObject(Map<String, Object> adResp) throws InvalideRequestException {
        try{
            Ad ad = new Ad();
            if(adResp.get(FIELD_ID) != null) ad.setId((Integer)adResp.get(FIELD_ID));
            if(adResp.get(FIELD_CREATIVE_1) != null)ad.setCreative1((String)adResp.get(FIELD_CREATIVE_1));
            if(adResp.get(FIELD_CREATIVE_2) != null)ad.setCreative2((String)adResp.get(FIELD_CREATIVE_2));
            if(adResp.get(FIELD_CREATIVE_3) != null)ad.setCreative3((String)adResp.get(FIELD_CREATIVE_3));
            if(adResp.get(FIELD_CLICKTHRU_TEXT) != null)ad.setClickthruText((String)adResp.get(FIELD_CLICKTHRU_TEXT));
            if(adResp.get(FIELD_CLICKTHRU_URL) != null)ad.setClickthruUrl((String)adResp.get(FIELD_CLICKTHRU_URL));
            if(adResp.get(FIELD_REMOVED) != null)ad.setRemoved((boolean)adResp.get(FIELD_REMOVED));
            if(adResp.get(FIELD_STATUS) != null)ad.setStatus((String)adResp.get(FIELD_STATUS));
            if(adResp.get(FIELD_CREATE_DATE) != null)ad.setCreateDate(new DateTime(adResp.get(FIELD_CREATE_DATE)));
            if(adResp.get(FIELD_GROUP_ID) != null)ad.setGroupId((Integer)adResp.get(FIELD_GROUP_ID));
            if(adResp.get(FIELD_PREMISE_MODE) != null)ad.setPremiseMode((String)adResp.get(FIELD_PREMISE_MODE));
            if(adResp.get(FIELD_PREMISE_ID) != null)ad.setPremiseId((Integer)adResp.get(FIELD_PREMISE_ID));

            return ad; 
        } catch (NumberFormatException ex){
            throw new InvalideRequestException(ex);
        }
        
    }
    
}
