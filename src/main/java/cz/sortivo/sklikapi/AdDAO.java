package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalidRequestException;
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
    private static final String RESTORE_AD_METHOD_NAME = "ad.remove";
    private static final String SET_ATTRIBUTES_METHOD_NAME = "ad.setAttributes";
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
    
    public List<Ad> listAds(int groupId) throws InvalidRequestException, SKlikException{
        Map<String, Object> response = client.sendRequest(LIST_ADS_METHOD_NAME, new Object[]{groupId});
        List<Ad> ads = new ArrayList<>();
        Object[] adsResp = (Object[]) response.get("ads");
        
        for (Object adResp : adsResp) {
            ads.add(transformToObject((Map<String, Object>)adResp));
        }
        return ads;
    }
    
   /**
    * 
    * @param groupId
    * @param ad
    * @return id of new Ad
    * @throws InvalidRequestException
    * @throws SKlikException 
    */
    public Integer create(int groupId, Ad ad) throws InvalidRequestException, SKlikException{
        Map<String, Object> resp = client.sendRequest(CREATE_AD_METHOD_NAME, new Object[]{groupId, transformFromObject(ad)});
        return (Integer)resp.get("adId");
    }
    
    public boolean remove(int adId) throws InvalidRequestException, SKlikException{
        client.sendRequest(REMOVE_AD_METHOD_NAME, new Object[]{adId});
        return true;
    }
    
    public void restore(int adId) throws InvalidRequestException, SKlikException{
        client.sendRequest(RESTORE_AD_METHOD_NAME, new Object[]{adId});
    }
    
    public boolean setActive(int adId) throws InvalidRequestException, SKlikException{
        return setAttributes(adId, new Attributes(Status.ACTIVE));
    }
    
    public boolean setSuspend(int adId) throws InvalidRequestException, SKlikException{
        return setAttributes(adId, new Attributes(Status.SUSPEND));
    }
    
    /**
     * Return existing ad object by id 
     * @param adId - unique sklik ad id
     * @return - ad object
     * @throws InvalidRequestException
     * @throws SKlikException
     */
    public Ad getAttributes(Long adId) throws InvalidRequestException, SKlikException{
        return transformToObject(client.sendRequest(GET_ATTRIBUTES_METHOD_NAME, new Object[]{adId}));
    }
    
    public boolean setAttributes(int adId, Attributes attributes) throws InvalidRequestException, SKlikException{
        Map<String, Object> map = new HashMap<>();
        map.put(FIELD_STATUS, attributes.getStatus().getStatusText());
        client.sendRequest(SET_ATTRIBUTES_METHOD_NAME, new Object[]{adId, map});
        return true;
    }
    
    public boolean setAttributes(int adId, Ad ad) throws InvalidRequestException, SKlikException{
        Map<String, Object> attributes = transformFromObject(ad);
        client.sendRequest(SET_ATTRIBUTES_METHOD_NAME, new Object[]{adId, attributes});
        return true;
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
