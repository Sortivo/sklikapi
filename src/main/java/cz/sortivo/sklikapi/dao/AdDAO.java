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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.sortivo.sklikapi.Client;
import cz.sortivo.sklikapi.EntityType;
import cz.sortivo.sklikapi.RequestIdMappedResponseUtils;
import cz.sortivo.sklikapi.ResponseUtils;
import cz.sortivo.sklikapi.Status;
import cz.sortivo.sklikapi.bean.Ad;
import cz.sortivo.sklikapi.bean.Response;
import cz.sortivo.sklikapi.exception.AdCreationException;
import cz.sortivo.sklikapi.exception.EntityCreationException;
import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * Allows manipulation with ads through API. The valid client session must be
 * specified by login operation before calling any other method. If using
 * multi-account access, the userId must be specified by calling it's setter on
 * the client's object.
 * 
 * @author Michal Ličko licko61@gmail.com (C) 2014
 */
public class AdDAO extends AbstractDAO<Ad> {

    Logger logger = LoggerFactory.getLogger(AdDAO.class);

    private static final String LIST_ADS_METHOD_NAME = "ads.list";
    private static final String UPDATE_METHOD_NAME = "ads.update";
    private static final String CREATE_METHOD_NAME = "ads.create";

    public static final int LIMIT_ADS_TO_CREATE = 100;
    public static final int LIMIT_ADS_TO_UPDATE = 100;

    private static final String FIELD_ID = "id";
    private static final String FIELD_CREATIVE_1 = "creative1";
    private static final String FIELD_CREATIVE_2 = "creative2";
    private static final String FIELD_CREATIVE_3 = "creative3";
    private static final String FIELD_CLICKTHRU_TEXT = "clickthruText";
    private static final String FIELD_CLICKTHRU_URL = "clickthruUrl";
    private static final String FIELD_DELETED = "deleted";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CREATE_DATE = "createDate";
    private static final String FIELD_GROUP_ID = "groupId";
    private static final String FIELD_PREMISE_MODE = "premiseMode";
    private static final String FIELD_PREMISE_ID = "premiseId";

    private static final String FIELD_REQUEST_ID = "requestId";

    private static final Set<String> CREATE_METHOD_ALLOWED_FIELDS = new HashSet<>(Arrays.asList(new String[] {
            FIELD_GROUP_ID, FIELD_CREATIVE_1, FIELD_CREATIVE_2, FIELD_CREATIVE_3, FIELD_CLICKTHRU_TEXT,
            FIELD_CLICKTHRU_URL, FIELD_STATUS, FIELD_PREMISE_MODE, FIELD_PREMISE_ID }));

    private static final Set<String> UPDATE_METHOD_ALLOWED_FIELDS = new HashSet<>(Arrays.asList(new String[] {
            FIELD_ID, FIELD_CLICKTHRU_URL, FIELD_STATUS, FIELD_PREMISE_MODE, FIELD_PREMISE_ID }));

    public static final int LIMIT_ADS_TO_GET = 100;
  
    private ResponseUtils responseUtils = new RequestIdMappedResponseUtils("adIds");

    public AdDAO(Client client) {
        this.client = client;
    }

    /**
     * Performs ad listing using specified level and entity IDs. Only level that
     * is allowed to contain ads is acceptable as parameter. FE.: if level is
     * set to Campaign the IDs arrays is allowed to contain campaign's IDs only.
     * 
     * @param ids
     *            - list of entity IDs
     * @param level
     *            - specifies level where to look for the ads
     * @param includeDeleted
     *            - includes deleted ads in request result if true
     * @return list of found ads or empty list, null is not allowed
     * @throws InvalidRequestException
     *             if request or response is syntacticly incorrect for
     *             processing with XMLRPC client
     * @throws SKlikException
     *             if some error occurs while SKlik is processing API request
     * @throws IllegalArgumentException
     *             if unsupported or null level is defined
     */
    public List<Ad> listAds(List<Integer> ids, EntityType level, boolean includeDeleted)
            throws InvalidRequestException, SKlikException {

        Map<String, Object> restrictionFilter = new LinkedHashMap<>();

        String mapIdsKeyName;

        logger.debug("Level set to " + level);
        switch (level) {
        case CAMPAIGN:
            mapIdsKeyName = "campaignIds";
            break;
        case GROUP:
            mapIdsKeyName = "groupIds";
            break;
        case AD:
            mapIdsKeyName = "adIds";
            break;
        default:
            throw new IllegalArgumentException("Unsupported level " + level);
        }
        restrictionFilter.put(mapIdsKeyName, ids);
        logger.debug("include deleted items in search: " + includeDeleted);
        restrictionFilter.put("includeDeleted", includeDeleted);

        Map<String, Object> response = client.sendRequest(LIST_ADS_METHOD_NAME, new Object[] { restrictionFilter });

        List<Ad> ads = new ArrayList<>();
        for (Object object : (Object[]) response.get("ads")) {
            ads.add(transformToObject((Map<String, Object>) object));
        }

        return ads;

    }

    public Integer create(int groupId, Ad ad) throws InvalidRequestException, SKlikException {
        return null;

    }

    public void setActive(int adId) throws InvalidRequestException, SKlikException {

    }

    /**
     * Performs creation of specified ads. API method is transactional. Id some
     * of specified ads contain errors, none of others will be proceeded.
     * 
     * @param ads
     * @return AdResponse object, may contain ad diagnostics if it was not
     *         processed correctly but was written to SKlik. Also contains ad
     *         object with updated id;
     * @throws InvalidRequestException
     *             if request or response is syntacticly incorrect for
     *             processing with XMLRPC client
     * @throws EntityCreationException
     *             - May be thrown if some of ads contains fatal error due to
     *             the whole transaction is rollbacked. Contains additional
     *             information with error cause and list of ads that caused
     *             errors.
     * @throws SKlikException
     */
    @Override
    public List<Response<Ad>> create(List<Ad> ads) throws InvalidRequestException, EntityCreationException, SKlikException {
        return save(ads, CREATE_METHOD_ALLOWED_FIELDS, CREATE_METHOD_NAME, LIMIT_ADS_TO_CREATE);

    }

    /**
     * Performs update operation on list of ads. Ad title, content and URLs
     * (creative1, creative2, creative3, clickthruText) cannot be changed. Old
     * ad will be deleted and new one will be created and newIds will be
     * provided in response.
     * 
     * @param ads
     * @return AdResponse object, may contain ad diagnostics if it was not
     *         processed correctly but was written to SKlik. Also may contain ad
     *         objects with updated id;
     * @throws InvalidRequestException
     *             if request or response is syntacticly incorrect for
     *             processing with XMLRPC client
     * @throws EntityCreationException
     *             - May be thrown if some of ads contains fatal error due to
     *             the whole transaction is rollbacked. Contains additional
     *             information with error cause and list of ads that caused
     *             errors.
     * @throws SKlikException
     */
    @Override
    public List<Response<Ad>> update(List<Ad> ads) throws InvalidRequestException, EntityCreationException, SKlikException {
        return save(ads, UPDATE_METHOD_ALLOWED_FIELDS, UPDATE_METHOD_NAME, LIMIT_ADS_TO_UPDATE);

    }


    
    

    /**
     * Performs pause operation on all ads with specified IDs.
     * 
     * @param ads
     *            list of ads to pause, count of ads for one update request id
     *            limited, call api.limits for more details or see
     *            LIMIT_ADS_TO_UPDATE for last known limit value
     * @return API response, may contain useful information if some errors
     *         appear
     * @throws InvalidRequestException
     *             if request or response is syntacticly incorrect for
     *             processing with XMLRPC client
     * @throws SKlikException
     *             if some error occurs while SKlik is processing API request
     */
    public Map<String, Object> pause(List<Ad> ads) throws InvalidRequestException, SKlikException {
        List<Map<String, Object>> adsList = new LinkedList<>();
        Map<String, Object> adMap;
        for (Ad ad : ads) {
            adMap = new LinkedHashMap<>();
            adMap.put(FIELD_ID, ad.getId());
            adMap.put(FIELD_STATUS, Status.SUSPEND.getStatusText());
            adsList.add(adMap);
        }

        return client.sendRequest(UPDATE_METHOD_NAME, new Object[] { adsList });
    }

    /**
     * Transforms an object to map, which can be send via XML-RPC
     * 
     * @param ad
     *            object that will be translated to map
     * @param FIELDS
     *            fields allowed by API method specification, if empty or null
     *            an empty map will be returned
     * @return Map containing object's attributes allowed by FIELDS definition
     */
    @Override
    protected Map<String, Object> transformFromObject(Ad ad, final Set FIELDS) {
        Map<String, Object> map = new HashMap<>();

        if (FIELDS == null) {
            return map;
        }

        if (ad.getId() != null && FIELDS.contains(FIELD_ID))
            map.put(FIELD_ID, ad.getId());
        if (ad.getCreative1() != null && FIELDS.contains(FIELD_CREATIVE_1))
            map.put(FIELD_CREATIVE_1, ad.getCreative1());
        if (ad.getCreative2() != null && FIELDS.contains(FIELD_CREATIVE_2))
            map.put(FIELD_CREATIVE_2, ad.getCreative2());
        if (ad.getCreative3() != null && FIELDS.contains(FIELD_CREATIVE_3))
            map.put(FIELD_CREATIVE_3, ad.getCreative3());
        if (ad.getClickthruText() != null && FIELDS.contains(FIELD_CLICKTHRU_TEXT))
            map.put(FIELD_CLICKTHRU_TEXT, ad.getClickthruText());
        if (ad.getClickthruUrl() != null && FIELDS.contains(FIELD_CLICKTHRU_URL))
            map.put(FIELD_CLICKTHRU_URL, ad.getClickthruUrl());
        if (FIELDS.contains(FIELD_DELETED)) {
            map.put(FIELD_DELETED, ad.isDeleted());
        }
        if (ad.getStatus() != null && FIELDS.contains(FIELD_STATUS))
            map.put(FIELD_STATUS, ad.getStatus().getStatusText());
        if (ad.getCreateDate() != null && FIELDS.contains(FIELD_CREATE_DATE))
            map.put(FIELD_CREATE_DATE, ad.getCreateDate());
        if (ad.getGroupId() != null && FIELDS.contains(FIELD_GROUP_ID))
            map.put(FIELD_GROUP_ID, ad.getGroupId());
        if (ad.getPremiseMode() != null && FIELDS.contains(FIELD_PREMISE_MODE))
            map.put(FIELD_PREMISE_MODE, ad.getPremiseMode());
        if (ad.getPremiseId() != null && FIELDS.contains(FIELD_PREMISE_ID))
            map.put(FIELD_PREMISE_ID, ad.getPremiseId());

        return map;
    }

    private Ad transformToObject(Map<String, Object> adResp) throws InvalidRequestException {
        try {
            Ad ad = new Ad();
            if (adResp.get(FIELD_ID) != null)
                ad.setId((Integer) adResp.get(FIELD_ID));
            if (adResp.get(FIELD_CREATIVE_1) != null)
                ad.setCreative1((String) adResp.get(FIELD_CREATIVE_1));
            if (adResp.get(FIELD_CREATIVE_2) != null)
                ad.setCreative2((String) adResp.get(FIELD_CREATIVE_2));
            if (adResp.get(FIELD_CREATIVE_3) != null)
                ad.setCreative3((String) adResp.get(FIELD_CREATIVE_3));
            if (adResp.get(FIELD_CLICKTHRU_TEXT) != null)
                ad.setClickthruText((String) adResp.get(FIELD_CLICKTHRU_TEXT));
            if (adResp.get(FIELD_CLICKTHRU_URL) != null)
                ad.setClickthruUrl((String) adResp.get(FIELD_CLICKTHRU_URL));
            if (adResp.get(FIELD_DELETED) != null)
                ad.setDeleted((boolean) adResp.get(FIELD_DELETED));
            if (adResp.get(FIELD_STATUS) != null)
                ad.setStatus(Status.getStatus((String) adResp.get(FIELD_STATUS)));
            if (adResp.get(FIELD_CREATE_DATE) != null)
                ad.setCreateDate(new DateTime(adResp.get(FIELD_CREATE_DATE)));
            if (adResp.get(FIELD_GROUP_ID) != null)
                ad.setGroupId((Integer) adResp.get(FIELD_GROUP_ID));
            if (adResp.get(FIELD_PREMISE_MODE) != null)
                ad.setPremiseMode((String) adResp.get(FIELD_PREMISE_MODE));
            if (adResp.get(FIELD_PREMISE_ID) != null)
                ad.setPremiseId((Integer) adResp.get(FIELD_PREMISE_ID));

            return ad;
        } catch (NumberFormatException ex) {
            throw new InvalidRequestException(ex);
        }

    }

    /**
     * 
     * @Deprecated No longer supported. Use listAds(List<Integer>, EntityType, boolean) instead 
     */
    @Deprecated()
    public List<Ad> listAds(int intValue) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 
     * @Deprecated No longer supported. Use update(List<Ad>) instead
     */
    @Deprecated()
    public void setAttributes(Integer id, Ad ad) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean supportsRequestId() {
        return true;
    }

    @Override
    protected ResponseUtils getResponseUtils() {
        return responseUtils;
    }

    @Override
    protected EntityCreationException getCreationException(String message, List<Response<Ad>> entityResponses,
            Throwable cause) {
        
        return new AdCreationException("sds", entityResponses, cause);
    }

    public void setSuspend(Integer id)throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub
        
    }



}
