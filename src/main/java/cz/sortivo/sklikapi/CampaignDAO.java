package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalideRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * http://api.sklik.cz/listCampaigns.html
 * @author Jan Dufek
 */
public class CampaignDAO {
    
    private static final String LIST_CAMPAIGNS_METHOD_NAME = "listCampaigns";
    private static final String CREATE_CAMPAIGN_METHOD_NAME = "campaign.create";
    private static final String REMOVE_CAMPAIGN_METHOD_NAME = "campaign.remove";
    
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
    
    public List<Campaign> listCampaigns() throws InvalideRequestException, SKlikException{
        return listCampaigns(new Object[]{}, false);
    }
    
    public List<Campaign> listCampaigns(int userId) throws InvalideRequestException, SKlikException{
        return listCampaigns(new Object[]{userId}, false);
    }
    
    public List<Campaign> listActiveCampaigns() throws InvalideRequestException, SKlikException{
        return listCampaigns(new Object[]{}, true);
    }
    
    public List<Campaign> listActiveCampaigns(int userId) throws InvalideRequestException, SKlikException{
        return listCampaigns(new Object[]{userId}, true);
    }    
    
    private List<Campaign> listCampaigns(Object[] params, boolean onlyActive) throws InvalideRequestException, SKlikException{
        Map<String, Object> response = client.sendRequest(LIST_CAMPAIGNS_METHOD_NAME, params);
        List<Campaign> campaigns = new ArrayList<>();
        Object[] respCampaigns = (Object[]) response.get("campaigns");
        
        for (Object respCampaign : respCampaigns) {
            Campaign campaign = transformToObject((Map<String, Object>)respCampaign);
            if (onlyActive) {
                if (!campaign.isRemoved() && campaign.getStatus().equals(ACTIVE_STATUS)) {
                    campaigns.add(campaign);
                }
            } else {
                campaigns.add(campaign);
            }
        }
        return campaigns;
    }
    
    /**
     * 
     * @param campaign
     * @return ID of new Campaign
     * @throws InvalideRequestException
     * @throws SKlikException 
     */
    public Integer create(Campaign campaign) throws InvalideRequestException, SKlikException{
        return create(new Object[]{transformFromObject(campaign)});
    }
    /**
     * 
     * @param campaign
     * @param userId
     * @return ID of new Campaign
     * @throws InvalideRequestException
     * @throws SKlikException 
     */
    public Integer create(Campaign campaign, int userId) throws InvalideRequestException, SKlikException{
        return create(new Object[]{transformFromObject(campaign), userId});
    }
    
    private Integer create(Object[] params) throws InvalideRequestException, SKlikException{
        Map<String, Object> resp = client.sendRequest(CREATE_CAMPAIGN_METHOD_NAME, params);
        return (Integer)resp.get("campaignId");
    }
    
    public boolean remove(int campaignId) throws InvalideRequestException, SKlikException{
        client.sendRequest(REMOVE_CAMPAIGN_METHOD_NAME, new Object[]{campaignId});
        return true;
    }
    
    private Map<String, Object> transformFromObject(Campaign campaign){
        Map<String, Object> map = new HashMap<>();
        map.put(FIELD_ID, campaign.getId());
        map.put(FIELD_NAME, campaign.getName());
        map.put(FIELD_REMOVED, campaign.isRemoved());
        map.put(FIELD_STATUS, campaign.getStatus());
        map.put(FIELD_DAY_BUDGET, campaign.getDayBudget());
        map.put(FIELD_AD_SELECTION, campaign.getAdSelection());
        map.put(FIELD_CREATE_DATE, campaign.getCreateDate());
        map.put(FIELD_TOTAL_BUDGET, campaign.getTotalBudget());
        map.put(FIELD_TOTAL_CLICKS, campaign.getTotalClicks());
        map.put(FIELD_PREMISE_ID, campaign.getPremiseId());
        map.put(FIELD_START_DATE, campaign.getStartDate());
        map.put(FIELD_END_DATE, campaign.getEndDate());
        return map;
    }

    private Campaign transformToObject(Map<String, Object> campaignResp) throws InvalideRequestException {
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
            throw new InvalideRequestException(ex);
        }
        
    }
}
