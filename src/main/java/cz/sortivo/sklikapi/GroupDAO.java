package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;

/**
 * http://api.sklik.cz/listGroups.html
 * @author Jan Dufek
 */
public class GroupDAO {
    private static final String LIST_GROUPS_METHOD_NAME = "listGroups";
    private static final String CREATE_GROUP_METHOD_NAME = "group.create";
    private static final String REMOVE_GROUP_METHOD_NAME = "group.remove";
    private static final String RESTORE_GROUP_METHOD_NAME = "group.restore";
    private static final String SET_ATTRIBUTES_METHOD_NAME = "group.setAttributes";
    private static final String GET_ATTRIBUTES_METHOD_NAME = "group.getAttributes";
    
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_REMOVED = "removed";
    private static final String FIELD_CPC = "cpc";
    private static final String FIELD_CPC_CONTEXT = "cpcContext";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CAMPAIGN_ID = "campaignId";
    private static final String FIELD_CREATE_DATE = "createDate";
    
    private Client client;
    
    public GroupDAO(Client client) {
        this.client = client;
    }
    
    public List<Group> listGroups(int campaignId) throws InvalidRequestException, SKlikException{
        Map<String, Object> resp = client.sendRequest(LIST_GROUPS_METHOD_NAME, new Object[]{campaignId});
        List<Group> groups = new ArrayList<>();
        Object[] groupsResp = (Object[]) resp.get("groups");
        
        for (Object groupResp : groupsResp) {
            groups.add(transformToObject((Map<String, Object>)groupResp));
        }
        return groups;
        
    }
    /**
     * 
     * @param campaignId
     * @param group
     * @return Id of new Group
     * @throws InvalidRequestException
     * @throws SKlikException 
     */
    public Integer create(int campaignId, Group group) throws InvalidRequestException, SKlikException{
        Map<String, Object> resp = client.sendRequest(CREATE_GROUP_METHOD_NAME, new Object[]{campaignId, transformFromObject(group)});
        return (Integer)resp.get("groupId");
    }
    
    public boolean remove(int groupId) throws InvalidRequestException, SKlikException{
        client.sendRequest(REMOVE_GROUP_METHOD_NAME, new Object[]{groupId});
        return true;
    }
    
    public void restore(int groupId )throws InvalidRequestException, SKlikException{
        client.sendRequest(RESTORE_GROUP_METHOD_NAME, new Object[]{groupId});
    }
    
    public boolean setActive(int groupId) throws InvalidRequestException, SKlikException{
        return setAttributes(groupId, new Attributes(Status.ACTIVE));
    }
    
    public boolean setSuspend(int groupId) throws InvalidRequestException, SKlikException{
        return setAttributes(groupId, new Attributes(Status.SUSPEND));
    }
    
    public boolean setKeywordMaxCpc(int groupId, int cpc)throws InvalidRequestException, SKlikException{
        return setAttributes(groupId, new Attributes(Status.SUSPEND));
    }
    
    public boolean setAttributes(int groupId, Attributes attributes) throws InvalidRequestException, SKlikException{
        Map<String, Object> map = new HashMap<>();
        map.put(FIELD_STATUS, attributes.getStatus().getStatusText());
        client.sendRequest(SET_ATTRIBUTES_METHOD_NAME, new Object[]{groupId, map});
        return true;
    }
    
    public boolean setAttributes(Group group)throws InvalidRequestException, SKlikException{
        client.sendRequest(SET_ATTRIBUTES_METHOD_NAME, new Object[]{group.getId(), transformFromObject(group)});
        return true;
    }
    
    public Group getAttributes(int groupId) throws InvalidRequestException, SKlikException{
        return transformToObject((Map<String, Object>)client.sendRequest(GET_ATTRIBUTES_METHOD_NAME, new Object[]{groupId}).get("group"));
    }
    
    private Map<String, Object> transformFromObject(Group g){
        Map<String, Object> map = new HashMap<>();
        if (g.getId() != null)map.put(FIELD_ID, g.getId());
        if (g.getName() != null)map.put(FIELD_NAME, g.getName());
        map.put(FIELD_REMOVED, g.isRemoved());
        if (g.getCpc() != null)map.put(FIELD_CPC, g.getCpc());
        if (g.getCpcContext() != null)map.put(FIELD_CPC_CONTEXT, g.getCpcContext());
        if (g.getStatus() != null)map.put(FIELD_STATUS, g.getStatus().getStatusText());
        if (g.getCampaignId() != null)map.put(FIELD_CAMPAIGN_ID, g.getCampaignId());
        if (g.getCreateDate() != null)map.put(FIELD_CREATE_DATE, g.getCreateDate());
        return map;
    }

    private Group transformToObject(Map<String, Object> groupRes) throws InvalidRequestException {
        try{
            Group g = new Group();
            if(groupRes.get(FIELD_ID) != null) g.setId((Integer)groupRes.get(FIELD_ID));
            if(groupRes.get(FIELD_NAME) != null)g.setName((String)groupRes.get(FIELD_NAME));
            if(groupRes.get(FIELD_REMOVED) != null)g.setRemoved((boolean)groupRes.get(FIELD_REMOVED));
            if(groupRes.get(FIELD_CPC) != null)g.setCpc((Integer)groupRes.get(FIELD_CPC));
            if(groupRes.get(FIELD_CPC_CONTEXT) != null)g.setCpcContext((Integer)groupRes.get(FIELD_CPC_CONTEXT));
            if(groupRes.get(FIELD_STATUS) != null)g.setStatus(Status.getStatus((String)groupRes.get(FIELD_STATUS)));
            if(groupRes.get(FIELD_CAMPAIGN_ID) != null)g.setCampaignId((Integer)groupRes.get(FIELD_CAMPAIGN_ID));
            if(groupRes.get(FIELD_CREATE_DATE) != null)g.setCreateDate(new DateTime(groupRes.get(FIELD_CREATE_DATE)));
            return g; 
        } catch (NumberFormatException ex){
            throw new InvalidRequestException(ex);
        }
        
    }
}
