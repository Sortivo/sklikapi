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

import cz.sortivo.sklikapi.Attributes;
import cz.sortivo.sklikapi.Client;
import cz.sortivo.sklikapi.IndexMappedResponseUtils;
import cz.sortivo.sklikapi.MatchType;
import cz.sortivo.sklikapi.ResponseUtils;
import cz.sortivo.sklikapi.Status;
import cz.sortivo.sklikapi.bean.Ad;
import cz.sortivo.sklikapi.bean.Keyword;
import cz.sortivo.sklikapi.bean.Response;
import cz.sortivo.sklikapi.exception.EntityCreationException;
import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.KeywordCreationException;
import cz.sortivo.sklikapi.exception.SKlikException;

public class KeywordDAO extends AbstractDAO<Keyword> {

    private static final Logger logger = LoggerFactory.getLogger(KeywordDAO.class);

    private static final String LIST_KEYWORDS_METHOD_NAME = "keywords.list";
    private static final String CREATE_KEYWORD_METHOD_NAME = "keywords.create";
    private static final String UPDATE_METHOD_NAME = "keywords.update";
    private static final String REMOVE_KEYWORD_METHOD_NAME = "keyword.remove";
    private static final String RESTORE_KEYWORD_METHOD_NAME = "keyword.restore";

    public static final int LIMIT_KEYWORDS_TO_CREATE = 5000;
    public static final int LIMIT_KEYWORDS_TO_UPDATE = 5000;

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_MATCH_TYPE = "matchType";
    private static final String FIELD_REMOVED = "deleted";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_DISABLED = "disabled";
    private static final String FIELD_CPC = "cpc";
    private static final String FIELD_URL = "url";
    private static final String FIELD_CREATE_DATE = "createDate";
    private static final String FIELD_GROUP = "group";
    private static final String FIELD_GROUP_ID = "groupId";
    private static final String FIELD_GROUP_GROUP_ID = "id";

    private static final String FIELD_MIN_CPC = "minCpc";

    private static final Set<String> CREATE_METHOD_ALLOWED_FIELDS = new HashSet<>(Arrays.asList(new String[] {
            FIELD_NAME, FIELD_GROUP_ID, FIELD_MATCH_TYPE, FIELD_CPC, FIELD_URL, FIELD_STATUS }));

    private static final Set<String> UPDATE_METHOD_ALLOWED_FIELDS = new HashSet<>(Arrays.asList(new String[] {
            FIELD_ID, FIELD_CPC, FIELD_URL, FIELD_STATUS }));
    
    private ResponseUtils responseUtils = new IndexMappedResponseUtils("positiveKeywordIds ");

    private static final Set<String> settableAttributes = new HashSet<>();

    public static final int LIMIT_KEYWORDS_TO_GET = 5000;
    static {
        settableAttributes.addAll(Arrays.asList(new String[] { FIELD_CPC, FIELD_URL, FIELD_STATUS }));
    }


    public KeywordDAO(Client client) {
        this.client = client;
    }

    public List<Keyword> listKeywords(List<Integer> groupIds, boolean includeDeleted) throws InvalidRequestException,
            SKlikException {
        List<Keyword> keywords = new ArrayList<>();

        Map<String, Object> restrictionFilter = new LinkedHashMap<>();
        restrictionFilter.put("groupIds", groupIds);
        restrictionFilter.put("includeDeleted", includeDeleted);

        Map<String, Object> response = client
                .sendRequest(LIST_KEYWORDS_METHOD_NAME, new Object[] { restrictionFilter });

        for (Object object : (Object[]) response.get("keywords")) {
            keywords.add(transformToObject((Map<String, Object>) object));
        }

        return keywords;
    }

    /**
     * Performs pause operation on all keywords with specified IDs.
     * 
     * @param keywords
     *            list of keywords to pause, count of keywords for one update
     *            request id limited, call api.limits for more details or see
     *            LIMIT_KEYWORDS_TO_UPDATE for last known limit value
     * @return API response, may contain useful information if some errors
     *         appear
     * @throws InvalidRequestException
     *             if request or response is syntacticly incorrect for
     *             processing with XMLRPC client
     * @throws SKlikException
     *             if some error occurs while SKlik is processing API request
     */
    public Map<String, Object> pause(List<Keyword> keywords) throws InvalidRequestException, SKlikException {

        if (keywords == null) {
            throw new IllegalArgumentException("Keywords cannot be null");
        }

        if (keywords.size() > LIMIT_KEYWORDS_TO_UPDATE) {
            throw new IllegalArgumentException(
                    "Count of keywords to pause exceeds allowed API limit, see api.limits for current details");
        }

        List<Map<String, Object>> kwsList = new LinkedList<>();
        Map<String, Object> kwMap;
        for (Keyword keyword : keywords) {

            kwMap = new LinkedHashMap<>();
            // check for the negative match type
            if (keyword.getMatchType() == MatchType.NEGATIVE_BROAD) {
                logger.warn("Trying to pause negative keyword which seems not to be pausable. Skipping ...");
                continue;
            }

            // set required fields
            kwMap.put(FIELD_ID, keyword.getId());
            kwMap.put(FIELD_STATUS, Status.SUSPEND.getStatusText());
            kwsList.add(kwMap);
        }

        return client.sendRequest(UPDATE_METHOD_NAME, new Object[] { kwsList });
    }


    private void checkMatchType(Keyword keyword) {
        if (keyword.getMatchType() == null) {
            String keywordName = keyword.getName().trim();

            if (keywordName.startsWith("\"") && keywordName.endsWith("\"") && keywordName.length() >= 2) {
                keyword.setMatchType(MatchType.PHRASE);
                keyword.setName(keywordName.substring(1, keywordName.length() - 1));
            } else if (keywordName.startsWith("[") && keywordName.endsWith("]")) {
                keyword.setMatchType(MatchType.EXACT);
                keyword.setName(keywordName.substring(1, keywordName.length() - 1));
            } else {
                keyword.setMatchType(MatchType.BROAD);
                keyword.setName(keywordName);
            }
        }

    }

    @Override
    protected Map<String, Object> transformFromObject(Keyword keyword, final Set<String> ALLOWED_FIELDS) {
        checkMatchType(keyword);
        Map<String, Object> map = new HashMap<>();

        if (keyword.getId() != null && ALLOWED_FIELDS.contains(FIELD_ID))
            map.put(FIELD_ID, keyword.getId());
        if (keyword.getName() != null && ALLOWED_FIELDS.contains(FIELD_NAME))
            map.put(FIELD_NAME, keyword.getName());
        if (keyword.getMatchType() != null && ALLOWED_FIELDS.contains(FIELD_MATCH_TYPE))
            map.put(FIELD_MATCH_TYPE, keyword.getMatchType().getMatchTypeText());
        if (ALLOWED_FIELDS.contains(FIELD_REMOVED))
            map.put(FIELD_REMOVED, keyword.isRemoved());
        if (keyword.getStatus() != null && ALLOWED_FIELDS.contains(FIELD_STATUS))
            map.put(FIELD_STATUS, keyword.getStatus().getStatusText());
        if (ALLOWED_FIELDS.contains(FIELD_DISABLED))
            map.put(FIELD_DISABLED, keyword.isDisabled());
        if (keyword.getCpc() != null && ALLOWED_FIELDS.contains(FIELD_CPC)) {
            if (keyword.getCpc() == -1) {
                map.put(FIELD_CPC, null);
            } else {
                map.put(FIELD_CPC, keyword.getCpc());
            }
        }
        if (keyword.getCreateDate() != null && ALLOWED_FIELDS.contains(FIELD_CREATE_DATE))
            map.put(FIELD_CREATE_DATE, keyword.getCreateDate());
        if (keyword.getUrl() != null && ALLOWED_FIELDS.contains(FIELD_URL))
            map.put(FIELD_URL, keyword.getUrl());
        if (keyword.getGroupId() != null && ALLOWED_FIELDS.contains(FIELD_GROUP_ID))
            map.put(FIELD_GROUP_ID, keyword.getGroupId());
        if (keyword.getMinCpc() != null && ALLOWED_FIELDS.contains(FIELD_MIN_CPC)) {
            if (keyword.getMinCpc() == -1) {
                map.put(FIELD_MIN_CPC, null);
            } else {
                map.put(FIELD_MIN_CPC, keyword.getCpc());
            }
        }
        return map;
    }

    @SuppressWarnings("incomplete-switch")
    private void setMatchTypeToName(Keyword k) {
        String name = k.getName();
        switch (k.getMatchType()) {
        case PHRASE:
            k.setName("\"" + name + "\"");
            break;
        case EXACT:
            k.setName("[" + name + "]");
            break;
        case NEGATIVE_BROAD:
            k.setName("-" + name);
            break;
        case NEGATIVE_PHRASE:
            k.setName("-\"" + name + "\"");
            break;
        case NEGATIVE_EXACT:
            k.setName("-[" + name + "]");
            break;
        }
    }

    @SuppressWarnings("unchecked")
    private Keyword transformToObject(Map<String, Object> keywordResp) throws InvalidRequestException {
        try {
            Keyword keyword = new Keyword();
            if (keywordResp.get(FIELD_ID) != null)
                keyword.setId((Integer) keywordResp.get(FIELD_ID));
            if (keywordResp.get(FIELD_NAME) != null)
                keyword.setName((String) keywordResp.get(FIELD_NAME));
            if (keywordResp.get(FIELD_MATCH_TYPE) != null)
                keyword.setMatchType(MatchType.getMatchType((String) keywordResp.get(FIELD_MATCH_TYPE)));
            if (keywordResp.get(FIELD_REMOVED) != null)
                keyword.setRemoved((boolean) keywordResp.get(FIELD_REMOVED));
            if (keywordResp.get(FIELD_STATUS) != null)
                keyword.setStatus(Status.getStatus((String) keywordResp.get(FIELD_STATUS)));
            if (keywordResp.get(FIELD_DISABLED) != null)
                keyword.setDisabled((boolean) keywordResp.get(FIELD_DISABLED));
            if (keywordResp.get(FIELD_CPC) != null)
                keyword.setCpc((Integer) keywordResp.get(FIELD_CPC));
            if (keywordResp.get(FIELD_URL) != null)
                keyword.setUrl((String) keywordResp.get(FIELD_URL));
            if (keywordResp.get(FIELD_CREATE_DATE) != null)
                keyword.setCreateDate(new DateTime(keywordResp.get(FIELD_CREATE_DATE)));
            if (keywordResp.get(FIELD_GROUP) != null)
                keyword.setGroupId((Integer) ((Map<String, Object>) keywordResp.get(FIELD_GROUP))
                        .get(FIELD_GROUP_GROUP_ID));
            if (keywordResp.get(FIELD_MIN_CPC) != null)
                keyword.setMinCpc((Integer) keywordResp.get(FIELD_MIN_CPC));

            setMatchTypeToName(keyword);

            return keyword;
        } catch (NumberFormatException ex) {
            throw new InvalidRequestException(ex);
        }

    }

    public List<Keyword> listKeywords(int intValue) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setAttributes(Keyword kw) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub

    }

    public void setAttributes(Integer id, Attributes attributes) throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub

    }



    @Override
    protected boolean supportsRequestId() {
        return false;
    }

    @Override
    protected ResponseUtils getResponseUtils() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected EntityCreationException getCreationException(String message, List<Response<Keyword>> entityResponses,
            Throwable cause) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Response<Keyword>> create(List<Keyword> entities) throws InvalidRequestException,
            EntityCreationException, SKlikException {
        return save(entities, CREATE_METHOD_ALLOWED_FIELDS, CREATE_KEYWORD_METHOD_NAME, LIMIT_KEYWORDS_TO_CREATE);
    }

    @Override
    public List<Response<Keyword>> update(List<Keyword> entities) throws InvalidRequestException,
            EntityCreationException, SKlikException {
        return save(entities, UPDATE_METHOD_ALLOWED_FIELDS, UPDATE_METHOD_NAME, LIMIT_KEYWORDS_TO_UPDATE);
    }

    public long create(int intValue, Keyword kw) throws InvalidRequestException,
    SKlikException {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setSuspend(Integer id)throws InvalidRequestException, SKlikException {
        // TODO Auto-generated method stub
        
    }

}
