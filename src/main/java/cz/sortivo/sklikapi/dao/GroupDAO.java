package cz.sortivo.sklikapi.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import cz.sortivo.sklikapi.Client;
import cz.sortivo.sklikapi.IndexMappedResponseUtils;
import cz.sortivo.sklikapi.ResponseUtils;
import cz.sortivo.sklikapi.Status;
import cz.sortivo.sklikapi.bean.Group;
import cz.sortivo.sklikapi.bean.Response;
import cz.sortivo.sklikapi.exception.EntityCreationException;
import cz.sortivo.sklikapi.exception.GroupCreationException;
import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * 
 * @author Michal Ličko licko61@gmail.com (C) 2014
 */
public class GroupDAO extends AbstractDAO<Group> {
    private static final String LIST_GROUPS_METHOD_NAME = "groups.list";
    private static final String GET_GROUPS_METHOD_NAME = "groups.get";
    private static final String CREATE_GROUP_METHOD_NAME = "groups.create";
    private static final String REMOVE_GROUP_METHOD_NAME = "groups.remove";
    private static final String RESTORE_GROUP_METHOD_NAME = "groups.restore";
    private static final String UPDATE_METHOD_NAME = "groups.update";


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

    public static final int LIMIT_GROUPS_TO_CREATE = 100;
    public static final int LIMIT_GROUPS_TO_UPDATE = 100;
    public static final int LIMIT_GROUPS_TO_GET = 100;

    private static final Set<String> UPDATE_METHOD_ALLOWED_FIELDS = new HashSet<>(Arrays.asList(new String[] {
            FIELD_ID, FIELD_NAME, FIELD_STATUS, FIELD_CPC, FIELD_CPC_CONTEXT, FIELD_CPM,
            FIELD_MAX_USER_DAILY_IMPRESSION }));
    private static final Set<String> CREATE_METHOD_ALLOWED_FIELDS = new HashSet<>(Arrays.asList(new String[] {
            FIELD_CAMPAIGN_ID, FIELD_NAME, FIELD_CPC, FIELD_CPC_CONTEXT, FIELD_CPM, FIELD_STATUS,
            FIELD_MAX_USER_DAILY_IMPRESSION }));

    private ResponseUtils responseUtils = new IndexMappedResponseUtils("groupIds ");


    public GroupDAO(Client client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public List<Group> listGroups(List<Integer> campaignIds, boolean includeDeleted) throws InvalidRequestException,
            SKlikException {

        Map<String, Object> restrictionFilter = new LinkedHashMap<>();
        restrictionFilter.put("includeDeleted", includeDeleted);
        restrictionFilter.put("campaignIds", campaignIds);

        Map<String, Object> response = client.sendRequest(LIST_GROUPS_METHOD_NAME, new Object[] { restrictionFilter });

        List<Group> groups = new ArrayList<>();
        for (Object object : (Object[]) response.get("groups")) {
            groups.add(transformToObject((Map<String, Object>) object));
        }

        return groups;

    }

    @SuppressWarnings("unchecked")
    public List<Group> listGroups(List<Integer> groupIds) throws InvalidRequestException, SKlikException {

        //check if parameter are valid
        if (groupIds == null)
            throw new IllegalAccessError("Group ids list cannot be null");
        if (groupIds.size() > LIMIT_GROUPS_TO_GET)
            throw new IllegalArgumentException(
                    "Group list exceeds a group count limit for GET operation. Current known limit value is: "
                            + LIMIT_GROUPS_TO_GET);

        Map<String, Object> restrictionFilter = new LinkedHashMap<>();
        restrictionFilter.put("campaignIds", groupIds);

        Map<String, Object> response = client.sendRequest(GET_GROUPS_METHOD_NAME, new Object[] { restrictionFilter });

        List<Group> groups = new ArrayList<>();
        for (Object object : (Object[]) response.get("groups")) {
            groups.add(transformToObject((Map<String, Object>) object));
        }

        return groups;

    }

    /**
     * Performs suspend operation on all specified groups
     * 
     * @param groups
     * @param userId
     * @return
     * @throws InvalidRequestException
     * @throws SKlikException
     */
    public Map<String, Object> pause(List<Group> groups) throws InvalidRequestException, SKlikException {
        List<Map<String, Object>> groupsList = new LinkedList<>();
        Map<String, Object> groupMap;
        for (Group group : groups) {
            groupMap = new LinkedHashMap<>();
            groupMap.put(FIELD_ID, group.getId());
            groupMap.put(FIELD_STATUS, Status.SUSPEND.getStatusText());
            groupsList.add(groupMap);
        }

        return client.sendRequest(UPDATE_METHOD_NAME, new Object[] { groupsList.toArray() });
    }

    @Override
    protected Map<String, Object> transformFromObject(Group g, Set<String> ALLOWED_FIELDS) {
        Map<String, Object> map = new HashMap<>();
        if (g.getId() != null && ALLOWED_FIELDS.contains(FIELD_ID))
            map.put(FIELD_ID, g.getId());
        if (g.getName() != null && ALLOWED_FIELDS.contains(FIELD_NAME))
            map.put(FIELD_NAME, g.getName());
        if (ALLOWED_FIELDS.contains(FIELD_REMOVED))
            map.put(FIELD_REMOVED, g.isRemoved());
        if (g.getCpc() != null && ALLOWED_FIELDS.contains(FIELD_CPC))
            map.put(FIELD_CPC, g.getCpc());
        if (g.getCpcContext() != null && ALLOWED_FIELDS.contains(FIELD_CPC_CONTEXT))
            map.put(FIELD_CPC_CONTEXT, g.getCpcContext());
        if (g.getStatus() != null && ALLOWED_FIELDS.contains(FIELD_STATUS))
            map.put(FIELD_STATUS, g.getStatus().getStatusText());
        if (g.getCampaignId() != null && ALLOWED_FIELDS.contains(FIELD_CAMPAIGN_ID))
            map.put(FIELD_CAMPAIGN_ID, g.getCampaignId());
        if (g.getCpm() != null && ALLOWED_FIELDS.contains(FIELD_CPM))
            map.put(FIELD_CPM, g.getCpm());
        if (g.getMaxUserDailyImpression() != null && ALLOWED_FIELDS.contains(FIELD_MAX_USER_DAILY_IMPRESSION))
            map.put(FIELD_MAX_USER_DAILY_IMPRESSION, g.getMaxUserDailyImpression());
        if (g.getCreateDate() != null && ALLOWED_FIELDS.contains(FIELD_CREATE_DATE))
            map.put(FIELD_CREATE_DATE, g.getCreateDate());

        return map;
    }

    private Group transformToObject(Map<String, Object> groupRes) throws InvalidRequestException {
        try {
            Group g = new Group();
            if (groupRes.get(FIELD_ID) != null)
                g.setId((Integer) groupRes.get(FIELD_ID));
            if (groupRes.get(FIELD_NAME) != null)
                g.setName((String) groupRes.get(FIELD_NAME));
            if (groupRes.get(FIELD_REMOVED) != null)
                g.setRemoved((boolean) groupRes.get(FIELD_REMOVED));
            if (groupRes.get(FIELD_CPC) != null)
                g.setCpc((Integer) groupRes.get(FIELD_CPC));
            if (groupRes.get(FIELD_CPC_CONTEXT) != null)
                g.setCpcContext((Integer) groupRes.get(FIELD_CPC_CONTEXT));
            if (groupRes.get(FIELD_STATUS) != null)
                g.setStatus(Status.getStatus((String) groupRes.get(FIELD_STATUS)));
            if (groupRes.get(FIELD_CAMPAIGN_ID) != null)
                g.setCampaignId((Integer) groupRes.get(FIELD_CAMPAIGN_ID));
            if (groupRes.get(FIELD_CREATE_DATE) != null)
                g.setCreateDate(new DateTime(groupRes.get(FIELD_CREATE_DATE)));
            if (groupRes.get(FIELD_MAX_USER_DAILY_IMPRESSION) != null)
                g.setMaxUserDailyImpression((Integer) groupRes.get(FIELD_MAX_USER_DAILY_IMPRESSION));
            if (groupRes.get(FIELD_CPM) != null)
                g.setCpm((Integer) groupRes.get(FIELD_CPM));
            return g;
        } catch (NumberFormatException ex) {
            throw new InvalidRequestException(ex);
        }

    }

    @Override
    protected boolean supportsRequestId() {
        return false;
    }

    @Override
    protected ResponseUtils getResponseUtils() {
        return responseUtils;
    }

    @Override
    protected EntityCreationException getCreationException(String message, List<Response<Group>> entityResponses,
            Throwable cause) {
        return new GroupCreationException(message, entityResponses, cause);
    }

    @Override
    public List<Response<Group>> create(List<Group> entities) throws InvalidRequestException, EntityCreationException,
            SKlikException {
        return save(entities, CREATE_METHOD_ALLOWED_FIELDS, CREATE_GROUP_METHOD_NAME, LIMIT_GROUPS_TO_CREATE);
    }

    @Override
    public List<Response<Group>> update(List<Group> entities) throws InvalidRequestException, EntityCreationException,
            SKlikException {
        return save(entities, UPDATE_METHOD_ALLOWED_FIELDS, UPDATE_METHOD_NAME, LIMIT_GROUPS_TO_UPDATE);
    }

    @Deprecated
    public List<Group> listGroups(int userId) throws InvalidRequestException, SKlikException {
        return null;
        // TODO
    }

    @Deprecated
    public Group getAttributes(int intValue) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub
        return null;
    }

    @Deprecated
    public void setActive(int intValue) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub

    }

    @Deprecated
    public void setAttributes(Group gr) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub

    }

    @Deprecated
    public long create(int intValue, Group group) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub
        return 0;
    }

}
