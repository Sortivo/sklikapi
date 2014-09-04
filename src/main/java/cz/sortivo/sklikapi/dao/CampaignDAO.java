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
import cz.sortivo.sklikapi.bean.Campaign;
import cz.sortivo.sklikapi.bean.Group;
import cz.sortivo.sklikapi.bean.Response;
import cz.sortivo.sklikapi.exception.CampaignCreationException;
import cz.sortivo.sklikapi.exception.EntityCreationException;
import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

public class CampaignDAO extends AbstractDAO<Campaign> {

    private static final String LIST_CAMPAIGNS_METHOD_NAME = "campaigns.list";
    private static final String GET_CAMPAIGNS_METHOD_NAME = "campaigns.get";
    private static final String CREATE_CAMPAIGN_METHOD_NAME = "campaigns.create";
    private static final String UPDATE_CAMPAIGN_METHOD_NAME = "campaigns.update";
    private static final String REMOVE_CAMPAIGN_METHOD_NAME = "campaigns.remove";
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

    private static final int LIMIT_CAMPAIGNS_TO_CREATE = 100;
    private static final int LIMIT_CAMPAIGNS_UPDATE = 100;

    private static final Set<String> UPDATE_METHOD_ALLOWED_FIELDS = new HashSet<>(Arrays.asList(new String[] {
            FIELD_ID, FIELD_NAME, FIELD_DAY_BUDGET, FIELD_TOTAL_BUDGET, FIELD_TOTAL_CLICKS, FIELD_AD_SELECTION,
            FIELD_START_DATE, FIELD_END_DATE, FIELD_STATUS }));
    private static final Set<String> CREATE_METHOD_ALLOWED_FIELDS = new HashSet<>(Arrays.asList(new String[] {
            FIELD_NAME, FIELD_DAY_BUDGET, FIELD_TOTAL_BUDGET, FIELD_TOTAL_CLICKS, FIELD_AD_SELECTION, FIELD_START_DATE,
            FIELD_END_DATE, FIELD_STATUS }));

    private ResponseUtils responseUtils = new IndexMappedResponseUtils("groupIds ");

    private Client client;

    public CampaignDAO(Client client) {
        this.client = client;
    }

    public List<Campaign> listCampaigns(boolean includeDeleted) throws InvalidRequestException, SKlikException {
        Map<String, Object> restrictionFilter = new LinkedHashMap<>();
        restrictionFilter.put("includeDeleted", includeDeleted);

        Map<String, Object> response = client.sendRequest(LIST_CAMPAIGNS_METHOD_NAME, new Object[] { restrictionFilter });

        List<Campaign> campaigns = new ArrayList<>();
        for (Object object : (Object[]) response.get("campaigns")) {
            campaigns.add(transformToObject((Map<String, Object>) object));
        }

        return campaigns;
    }
    
    @SuppressWarnings("unchecked")
    public List<Campaign> listCampaigns(List<Integer> campaignIds) throws InvalidRequestException, SKlikException{
        Map<String, Object> restrictionFilter = new LinkedHashMap<>();

        restrictionFilter.put("campaignIds", campaignIds);

        Map<String, Object> response = client.sendRequest(GET_CAMPAIGNS_METHOD_NAME, new Object[] { restrictionFilter });

        List<Campaign> campaigns = new ArrayList<>();
        for (Object object : (Object[]) response.get("groups")) {
            campaigns.add(transformToObject((Map<String, Object>) object));
        }

        return campaigns;
    }

    public List<Campaign> listCampaigns(int userId) throws InvalidRequestException, SKlikException {
        return null;
        // TODO
    }

    public Integer create(Campaign c) throws InvalidRequestException, SKlikException {
        return null;
        // TODO
    }

    public Integer create(Campaign c, int userId) throws InvalidRequestException, SKlikException {
        return null;
        // TODO
    }
    
    public Map<String, Object> pause(List<Campaign> campaigns) throws InvalidRequestException, SKlikException {
        List<Map<String, Object>> campsList = new LinkedList<>();
        Map<String, Object> campMap;
        for (Campaign campaign : campaigns) {
            campMap = new LinkedHashMap<>();
            campMap.put("id", campaign.getId());
            campMap.put("status", "suspend");
            campsList.add(campMap);
        }

        return client.sendRequest(UPDATE_METHOD_NAME, new Object[] { campsList });
    }

    @Override
    protected Map<String, Object> transformFromObject(Campaign campaign, Set<String> ALOWED_FIELDS) {
        Map<String, Object> map = new HashMap<>();
        if (campaign.getId() != null && ALOWED_FIELDS.contains(FIELD_ID))
            map.put(FIELD_ID, campaign.getId());
        if (campaign.getName() != null && ALOWED_FIELDS.contains(FIELD_NAME))
            map.put(FIELD_NAME, campaign.getName());
        if (campaign.getId() != null && ALOWED_FIELDS.contains(FIELD_REMOVED))
            map.put(FIELD_REMOVED, campaign.isRemoved());
        if (campaign.getStatus() != null && ALOWED_FIELDS.contains(FIELD_STATUS))
            map.put(FIELD_STATUS, campaign.getStatus());
        if (campaign.getDayBudget() != null && ALOWED_FIELDS.contains(FIELD_DAY_BUDGET))
            map.put(FIELD_DAY_BUDGET, campaign.getDayBudget());
        if (campaign.getAdSelection() != null && ALOWED_FIELDS.contains(FIELD_AD_SELECTION))
            map.put(FIELD_AD_SELECTION, campaign.getAdSelection());
        if (campaign.getCreateDate() != null && ALOWED_FIELDS.contains(FIELD_CREATE_DATE))
            map.put(FIELD_CREATE_DATE, campaign.getCreateDate());
        if (campaign.getTotalBudget() != null && ALOWED_FIELDS.contains(FIELD_TOTAL_BUDGET))
            map.put(FIELD_TOTAL_BUDGET, campaign.getTotalBudget());
        if (campaign.getTotalClicks() != null && ALOWED_FIELDS.contains(FIELD_TOTAL_CLICKS))
            map.put(FIELD_TOTAL_CLICKS, campaign.getTotalClicks());
        if (campaign.getPremiseId() != null && ALOWED_FIELDS.contains(FIELD_PREMISE_ID))
            map.put(FIELD_PREMISE_ID, campaign.getPremiseId());
        if (campaign.getStartDate() != null && ALOWED_FIELDS.contains(FIELD_START_DATE))
            map.put(FIELD_START_DATE, campaign.getStartDate());
        if (campaign.getEndDate() != null && ALOWED_FIELDS.contains(FIELD_END_DATE))
            map.put(FIELD_END_DATE, campaign.getEndDate());
        return map;
    }

    private Campaign transformToObject(Map<String, Object> campaignResp) throws InvalidRequestException {
        try {
            Campaign campaign = new Campaign();
            if (campaignResp.get(FIELD_ID) != null)
                campaign.setId((Integer) campaignResp.get(FIELD_ID));
            if (campaignResp.get(FIELD_NAME) != null)
                campaign.setName((String) campaignResp.get(FIELD_NAME));
            if (campaignResp.get(FIELD_REMOVED) != null)
                campaign.setRemoved((boolean) campaignResp.get(FIELD_REMOVED));
            if (campaignResp.get(FIELD_STATUS) != null)
                campaign.setStatus((String) campaignResp.get(FIELD_STATUS));
            if (campaignResp.get(FIELD_DAY_BUDGET) != null)
                campaign.setDayBudget((Integer) campaignResp.get(FIELD_DAY_BUDGET));
            if (campaignResp.get(FIELD_EXHAUSTED_DAY_BUDGET) != null)
                campaign.setExhaustedDayBudget((Integer) campaignResp.get(FIELD_EXHAUSTED_DAY_BUDGET));
            if (campaignResp.get(FIELD_AD_SELECTION) != null)
                campaign.setAdSelection((String) campaignResp.get(FIELD_AD_SELECTION));
            if (campaignResp.get(FIELD_CREATE_DATE) != null)
                campaign.setCreateDate(new DateTime(campaignResp.get(FIELD_CREATE_DATE)));
            if (campaignResp.get(FIELD_TOTAL_BUDGET) != null)
                campaign.setExhaustedTotalBudget((Integer) campaignResp.get(FIELD_TOTAL_BUDGET));
            if (campaignResp.get(FIELD_EXHAUSTED_TOTAL_BUDGET) != null)
                campaign.setExhaustedTotalBudget((Integer) campaignResp.get(FIELD_EXHAUSTED_TOTAL_BUDGET));
            if (campaignResp.get(FIELD_TOTAL_CLICKS) != null)
                campaign.setTotalClicks((Integer) campaignResp.get(FIELD_TOTAL_CLICKS));
            if (campaignResp.get(FIELD_EXHAUSTED_TOTAL_CLICKS) != null)
                campaign.setExhaustedTotalClicks((Integer) campaignResp.get(FIELD_EXHAUSTED_TOTAL_CLICKS));
            if (campaignResp.get(FIELD_PREMISE_ID) != null)
                campaign.setPremiseId((Integer) campaignResp.get(FIELD_PREMISE_ID));

            return campaign;
        } catch (NumberFormatException ex) {
            throw new InvalidRequestException(ex);
        }

    }

    public List<Campaign> listActiveCampaigns(Integer sklikUserId) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub
        return null;
    }

    public void pauseCampaign(Integer campaignId) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub

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
    protected EntityCreationException getCreationException(String message, List<Response<Campaign>> entityResponses,
            Throwable cause) {
        return new CampaignCreationException(message, entityResponses, cause);
    }

    @Override
    public List<Response<Campaign>> create(List<Campaign> entities) throws InvalidRequestException,
            EntityCreationException, SKlikException {
        return save(entities, CREATE_METHOD_ALLOWED_FIELDS, CREATE_CAMPAIGN_METHOD_NAME, LIMIT_CAMPAIGNS_TO_CREATE);
    }

    @Override
    public List<Response<Campaign>> update(List<Campaign> entities) throws InvalidRequestException,
            EntityCreationException, SKlikException {
        return save(entities, UPDATE_METHOD_ALLOWED_FIELDS, UPDATE_METHOD_NAME, LIMIT_CAMPAIGNS_UPDATE);
    }
}
