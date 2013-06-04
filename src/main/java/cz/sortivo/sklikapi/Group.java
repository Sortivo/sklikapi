package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalideRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * http://api.sklik.cz/listGroups.html
 * @author Jan Dufek
 */
public class Group {
    private static final String LIST_GROUPS_METHOD_NAME = "listGroups";
    private static final String CREATE_GROUP_METHOD_NAME = "group.create";
    private static final String REMOVE_GROUP_METHOD_NAME = "group.remove";
    
    private Client client;
    
    public Group(Client client) {
        this.client = client;
    }
    
    public Response listGroups(int campaignId) throws InvalideRequestException, SKlikException{
        return client.sendRequest(LIST_GROUPS_METHOD_NAME, new Object[]{campaignId});
    }
        
    public Response create(int campaignId, Struct group) throws InvalideRequestException, SKlikException{
        return client.sendRequest(CREATE_GROUP_METHOD_NAME, new Object[]{campaignId, group});
    }
    
    public Response remove(int groupId) throws InvalideRequestException, SKlikException{
        return client.sendRequest(REMOVE_GROUP_METHOD_NAME, new Object[]{groupId});
    }
}
