package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalideRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * http://api.sklik.cz/listCampaigns.html
 * @author Jan Dufek
 */
public class Campaign {
    
    private static final String LIST_CAMPAIGNS_METHOD_NAME = "listCampaigns";
    private static final String CREATE_CAMPAIGN_METHOD_NAME = "campaign.create";
    private static final String REMOVE_CAMPAIGN_METHOD_NAME = "campaign.remove";
    
    private Client client;
    
    public Campaign(Client client) {
        this.client = client;
    }
    
    public Response listCampaigns() throws InvalideRequestException, SKlikException{
        return client.sendRequest(LIST_CAMPAIGNS_METHOD_NAME, new Object[]{});
    }
    
    public Response listCampaigns(int userId) throws InvalideRequestException, SKlikException{
        return client.sendRequest(LIST_CAMPAIGNS_METHOD_NAME, new Object[]{userId});
    }
    
    public Response create(int groupId, Struct campaign) throws InvalideRequestException, SKlikException{
        return client.sendRequest(CREATE_CAMPAIGN_METHOD_NAME, new Object[]{groupId, campaign});
    }
    
    public Response remove(int campaignId) throws InvalideRequestException, SKlikException{
        return client.sendRequest(REMOVE_CAMPAIGN_METHOD_NAME, new Object[]{campaignId});
    }
}
