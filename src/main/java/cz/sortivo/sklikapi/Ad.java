package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalideRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * http://api.sklik.cz/listAds.html
 * @author Jan Dufek
 */
public class Ad {
    
    private static final String LIST_ADS_METHOD_NAME = "listAds";
    private static final String CREATE_AD_METHOD_NAME = "ad.create";
    private static final String REMOVE_AD_METHOD_NAME = "ad.remove";
    
    private Client client;
    
    public Ad(Client client) {
        this.client = client;
    }
    
    public Response listAds(int groupId) throws InvalideRequestException, SKlikException{
        return client.sendRequest(LIST_ADS_METHOD_NAME, new Object[]{groupId});
    }
    
    public Response create(int groupId, Struct ad) throws InvalideRequestException, SKlikException{
        return client.sendRequest(CREATE_AD_METHOD_NAME, new Object[]{groupId, ad});
    }
    
    public Response remove(int adId) throws InvalideRequestException, SKlikException{
        return client.sendRequest(REMOVE_AD_METHOD_NAME, new Object[]{adId});
    }
    
}
