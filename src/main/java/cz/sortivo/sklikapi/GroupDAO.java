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
 * 
 * @author Michal Ličko 
 * licko61@gmail.com
 * (C) 2014
 */
public class GroupDAO {
    private static final String LIST_GROUPS_METHOD_NAME = "groups.list";
    private static final String CREATE_GROUP_METHOD_NAME = "group.create";
    private static final String REMOVE_GROUP_METHOD_NAME = "group.remove";
    private static final String RESTORE_GROUP_METHOD_NAME = "group.restore";
    private static final String UPDATE_METHOD_NAME = "groups.update";
    private static final String CHECK_METHOD_NAME = "group.getAttributes";
    
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_REMOVED = "deleted";
    private static final String FIELD_CPC = "cpc";
    private static final String FIELD_CPC_CONTEXT = "cpcContext";
    private static final String FIELD_CPM = "cpm";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CAMPAIGN_ID = "campaignId";
    private static final String FIELD_CREATE_DATE = "createDate";
    private static final String FIELD_MAX_USER_DAILY_IMPRESSION = "maxUserDailyImpression";
    
    private Client client;
    
    public GroupDAO(Client client) {
        this.client = client;
    }
    

    @SuppressWarnings("unchecked")
    public List<Group> listGroups(List<Integer> campaignIds, boolean includeDeleted, Integer userId) throws InvalidRequestException, SKlikException{

        
        Map<String, Object> restrictionFilter = new LinkedHashMap<>();
        restrictionFilter.put("includeDeleted", includeDeleted);
        restrictionFilter.put("campaignIds", campaignIds);
        
        Map<String, Object> response = client.sendRequest(LIST_GROUPS_METHOD_NAME, new Object[]{restrictionFilter}, userId);
        
        List<Group> groups = new ArrayList<>();
        for (Object object : (Object[])response.get("groups")) {
            groups.add(transformToObject((Map<String, Object>) object));
        }
        
        return groups;
        
    }
   
    
    /**
     * Performs update of specified groups in SKlik.
     * @param groups
     * @param userId
     * @return
     * @throws InvalidRequestException
     * @throws SKlikException
     */
    public Map<String, Object> update(List<Group> groups, Integer userId) throws InvalidRequestException, SKlikException{
        
        List<Map<String, Object>> groupsList = new LinkedList<>();
        for (Group group : groups) {
            groupsList.add(transformFromObject(group));
        }
        
        return client.sendRequest(UPDATE_METHOD_NAME, new Object[]{groupsList.toArray()}, userId);
        
    }
    
    /**
     * Performs suspend operation on all specified groups 
     * @param groups
     * @param userId
     * @return
     * @throws InvalidRequestException
     * @throws SKlikException
     */
    public Map<String, Object> pause(List<Group> groups, Integer userId) throws InvalidRequestException, SKlikException{
        List<Map<String, Object>> groupsList = new LinkedList<>();
        Map<String, Object> groupMap;
        for (Group group : groups) {
            groupMap = new LinkedHashMap<>();
            groupMap.put("id", group.getId());
            groupMap.put("status", "suspend");
            groupsList.add(groupMap);
        }
        
        return client.sendRequest(UPDATE_METHOD_NAME, new Object[]{groupsList.toArray()}, userId);
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
        if (g.getCpm() != null)map.put(FIELD_CPM, g.getCpm());
        if (g.getMaxUserDailyImpression() != null)map.put(FIELD_MAX_USER_DAILY_IMPRESSION, g.getMaxUserDailyImpression());
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
            if(groupRes.get(FIELD_MAX_USER_DAILY_IMPRESSION) != null)g.setMaxUserDailyImpression((Integer) groupRes.get(FIELD_MAX_USER_DAILY_IMPRESSION));
            if(groupRes.get(FIELD_CPM) != null)g.setCpm((Integer) groupRes.get(FIELD_CPM));
            return g; 
        } catch (NumberFormatException ex){
            throw new InvalidRequestException(ex);
        }
        
    }
}
