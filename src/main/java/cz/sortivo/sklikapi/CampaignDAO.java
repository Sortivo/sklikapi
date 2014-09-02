package cz.sortivo.sklikapi;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import cz.sortivo.sklikapi.bean.Campaign;
import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * http://api.sklik.cz/listCampaigns.html
 * @author Jan Dufek
 */
public class CampaignDAO {
    
    private static final String LIST_CAMPAIGNS_METHOD_NAME = "listCampaigns";
    private static final String CREATE_CAMPAIGN_METHOD_NAME = "campaign.create";
    private static final String GET_ATTRIBUTES_METHOD_NAME = "campaign.getAttributes";
    private static final String REMOVE_CAMPAIGN_METHOD_NAME = "campaign.remove";
    private static final String UPDATE_METHOD_NAME = "campaigns.update";
    
    private static final String ACTIVE_STATUS = "active";
    
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_REMOVED = "removed";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_DAY_BUDGET = "dayBudget";
    private static final String FIELD_EXHAUSTED_DAY_BUDGET = "exhaustedDayBudget";
    private static final String FIELD_AD_SELECTION = "adSelection";
    private static final String FIELD_CREATE_DATE = "createDate";
    private static final String FIELD_TOTAL_BUDGET = "totalBudget";
    private static final String FIELD_EXHAUSTED_TOTAL_BUDGET = "exhaustedTotalBudget";
    private static final String FIELD_TOTAL_CLICKS = "totalClicks";
    private static final String FIELD_EXHAUSTED_TOTAL_CLICKS = "exhaustedTotalClicks";
    private static final String FIELD_PREMISE_ID = "premiseId";
    private static final String FIELD_START_DATE = "startDate";
    private static final String FIELD_END_DATE = "endDate";
    
    
    private Client client;
    
    public CampaignDAO(Client client) {
        this.client = client;
    }
    
    public List<Campaign> listCampaigns()throws InvalidRequestException, SKlikException{
        return null;
        //TODO 
    }
    
    public List<Campaign> listCampaigns(int userId)throws InvalidRequestException, SKlikException{
        return null;
        //TODO 
    }
    
  
    
    public Integer create(Campaign c)throws InvalidRequestException, SKlikException{
        return null;
        //TODO 
    }
    
    public Integer create(Campaign c, int userId)throws InvalidRequestException, SKlikException{
        return null;
        //TODO 
    }
    
    

    public Map<String, Object> pause(List<Campaign> campaigns) throws InvalidRequestException, SKlikException{
        List<Map<String, Object>> campsList = new LinkedList<>();
        Map<String, Object> campMap;
        for (Campaign campaign : campaigns) {
            campMap = new LinkedHashMap<>();
            campMap.put("id", campaign.getId());
            campMap.put("status", "suspend");
            campsList.add(campMap);
        }
        
        return client.sendRequest(UPDATE_METHOD_NAME, new Object[]{campsList});
    }
    
    private Map<String, Object> transformFromObject(Campaign campaign){
        Map<String, Object> map = new HashMap<>();
        if (campaign.getId() != null)map.put(FIELD_ID, campaign.getId());
        if (campaign.getName() != null)map.put(FIELD_NAME, campaign.getName());
        if (campaign.getId() != null)map.put(FIELD_REMOVED, campaign.isRemoved());
        if (campaign.getStatus() != null)map.put(FIELD_STATUS, campaign.getStatus());
        if (campaign.getDayBudget() != null)map.put(FIELD_DAY_BUDGET, campaign.getDayBudget());
        if (campaign.getAdSelection() != null)map.put(FIELD_AD_SELECTION, campaign.getAdSelection());
        if (campaign.getCreateDate() != null)map.put(FIELD_CREATE_DATE, campaign.getCreateDate());
        if (campaign.getTotalBudget() != null)map.put(FIELD_TOTAL_BUDGET, campaign.getTotalBudget());
        if (campaign.getTotalClicks() != null)map.put(FIELD_TOTAL_CLICKS, campaign.getTotalClicks());
        if (campaign.getPremiseId() != null)map.put(FIELD_PREMISE_ID, campaign.getPremiseId());
        if (campaign.getStartDate() != null)map.put(FIELD_START_DATE, campaign.getStartDate());
        if (campaign.getEndDate() != null)map.put(FIELD_END_DATE, campaign.getEndDate());
        return map;
    }

    private Campaign transformToObject(Map<String, Object> campaignResp) throws InvalidRequestException {
        try{
            Campaign campaign = new Campaign();
            if(campaignResp.get(FIELD_ID) != null) campaign.setId((Integer)campaignResp.get(FIELD_ID));
            if(campaignResp.get(FIELD_NAME) != null)campaign.setName((String)campaignResp.get(FIELD_NAME));
            if(campaignResp.get(FIELD_REMOVED) != null)campaign.setRemoved((boolean)campaignResp.get(FIELD_REMOVED));
            if(campaignResp.get(FIELD_STATUS) != null)campaign.setStatus((String)campaignResp.get(FIELD_STATUS));
            if(campaignResp.get(FIELD_DAY_BUDGET) != null)campaign.setDayBudget((Integer)campaignResp.get(FIELD_DAY_BUDGET));
            if(campaignResp.get(FIELD_EXHAUSTED_DAY_BUDGET) != null)campaign.setExhaustedDayBudget((Integer)campaignResp.get(FIELD_EXHAUSTED_DAY_BUDGET));
            if(campaignResp.get(FIELD_AD_SELECTION) != null)campaign.setAdSelection((String)campaignResp.get(FIELD_AD_SELECTION));
            if(campaignResp.get(FIELD_CREATE_DATE) != null)campaign.setCreateDate(new DateTime(campaignResp.get(FIELD_CREATE_DATE)));
            if(campaignResp.get(FIELD_TOTAL_BUDGET) != null)campaign.setExhaustedTotalBudget((Integer)campaignResp.get(FIELD_TOTAL_BUDGET));
            if(campaignResp.get(FIELD_EXHAUSTED_TOTAL_BUDGET) != null)campaign.setExhaustedTotalBudget((Integer)campaignResp.get(FIELD_EXHAUSTED_TOTAL_BUDGET));
            if(campaignResp.get(FIELD_TOTAL_CLICKS) != null)campaign.setTotalClicks((Integer)campaignResp.get(FIELD_TOTAL_CLICKS));
            if(campaignResp.get(FIELD_EXHAUSTED_TOTAL_CLICKS) != null)campaign.setExhaustedTotalClicks((Integer)campaignResp.get(FIELD_EXHAUSTED_TOTAL_CLICKS));
            if(campaignResp.get(FIELD_PREMISE_ID) != null)campaign.setPremiseId((Integer)campaignResp.get(FIELD_PREMISE_ID));

            return campaign; 
        } catch (NumberFormatException ex){
            throw new InvalidRequestException(ex);
        }
        
    }
}
