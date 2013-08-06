package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalideRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * http://api.sklik.cz/listKeywords.html
 * @author Jan Dufek
 */
public class KeywordDAO {
    private static final String LIST_KEYWORDS_METHOD_NAME = "listKeywords";
    private static final String CREATE_KEYWORD_METHOD_NAME = "keyword.create";
    private static final String REMOVE_KEYWORD_METHOD_NAME = "keyword.remove";
    private static final String SET_ATTRIBUTES_METHOD_NAME = "keyword.setAttributes";
    
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_MATCH_TYPE = "matchType";
    private static final String FIELD_REMOVED = "removed";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_DISABLED = "disabled";
    private static final String FIELD_CPC = "cpc";
    private static final String FIELD_URL = "url";
    private static final String FIELD_CREATE_DATE = "createDate";
    private static final String FIELD_GROUP_ID = "groupId";
    private static final String FIELD_MIN_CPC = "minCpc";
    
    private Client client;
    
    public KeywordDAO(Client client) {
        this.client = client;
    }
    
    public List<Keyword> listKeywords(int groupId) throws InvalideRequestException, SKlikException{
        Map<String, Object> response = client.sendRequest(LIST_KEYWORDS_METHOD_NAME, new Object[]{groupId});
        List<Keyword> keywords = new ArrayList<>();
        Object[] respKeywords = (Object[]) response.get("keywords");
        
        for (Object respCampaign : respKeywords) {
            keywords.add(transformToObject((Map<String, Object>)respCampaign));
        }
        return keywords;
    }
        
    /**
     * 
     * @param groupId
     * @param keyword
     * @return Id of new Keyword
     * @throws InvalideRequestException
     * @throws SKlikException 
     */
    public Integer create(int groupId, Keyword keyword) throws InvalideRequestException, SKlikException{
        Map<String, Object> resp = client.sendRequest(CREATE_KEYWORD_METHOD_NAME, new Object[]{groupId, transformFromObject(keyword)});
        return (Integer)resp.get("keywordId");
    }
    
    public boolean remove(int keywordId) throws InvalideRequestException, SKlikException{
        client.sendRequest(REMOVE_KEYWORD_METHOD_NAME, new Object[]{keywordId});
        return true;
    }
    
    public boolean setActive(int keywordId) throws InvalideRequestException, SKlikException{
        return setAttributes(keywordId, new Attributes(Status.ACTIVE));
    }
    
    public boolean setSuspend(int keywordId) throws InvalideRequestException, SKlikException{
        return setAttributes(keywordId, new Attributes(Status.SUSPEND));
    }
    
    public boolean setAttributes(int keywordId, Attributes attributes) throws InvalideRequestException, SKlikException{
        Map<String, Object> map = new HashMap<>();
        map.put(FIELD_STATUS, attributes.getStatus().getStatusText());
        client.sendRequest(SET_ATTRIBUTES_METHOD_NAME, new Object[]{keywordId, map});
        return true;
    }
    
    private void checkMatchType(Keyword keyword){
        if (keyword.getMatchType() == null){
            String keywordName = keyword.getName().trim();


            if (keywordName.startsWith("\"") && keywordName.endsWith("\"") && keywordName.length() >= 2){
                keyword.setMatchType(MatchType.PHRASE);
                keyword.setName(keywordName.substring(1, keywordName.length() - 1));
            } else if(keywordName.startsWith("[") && keywordName.endsWith("]")){
                keyword.setMatchType(MatchType.EXACT);
                keyword.setName(keywordName.substring(1, keywordName.length() - 1));
            } else {
                keyword.setMatchType(MatchType.BROAD);
                keyword.setName(keywordName);
            }
        }
           
    }
    
    private Map<String, Object> transformFromObject(Keyword keyword){
        checkMatchType(keyword);
        Map<String, Object> map = new HashMap<>();
        map.put(FIELD_ID, keyword.getId());
        map.put(FIELD_NAME, keyword.getName());
        map.put(FIELD_MATCH_TYPE, keyword.getMatchType().getMatchTypeText());
        map.put(FIELD_REMOVED, keyword.isRemoved());
        map.put(FIELD_STATUS, keyword.getStatus().getStatusText());
        map.put(FIELD_DISABLED, keyword.isDisabled());
        map.put(FIELD_CPC, keyword.getCpc());
        map.put(FIELD_CREATE_DATE, keyword.getCreateDate());
        map.put(FIELD_URL, keyword.getUrl());
        map.put(FIELD_GROUP_ID, keyword.getGroupId());
        map.put(FIELD_MIN_CPC, keyword.getMinCpc());
        return map;
    }

    private Keyword transformToObject(Map<String, Object> keywordResp) throws InvalideRequestException {
        try{
            Keyword keyword = new Keyword();
            if(keywordResp.get(FIELD_ID) != null) keyword.setId((Integer)keywordResp.get(FIELD_ID));
            if(keywordResp.get(FIELD_NAME) != null)keyword.setName((String)keywordResp.get(FIELD_NAME));
            if(keywordResp.get(FIELD_MATCH_TYPE) != null)keyword.setMatchType(MatchType.getMatchType((String)keywordResp.get(FIELD_MATCH_TYPE)));
            if(keywordResp.get(FIELD_REMOVED) != null)keyword.setRemoved((boolean)keywordResp.get(FIELD_REMOVED));
            if(keywordResp.get(FIELD_STATUS) != null)keyword.setStatus(Status.getStatus((String)keywordResp.get(FIELD_STATUS)));
            if(keywordResp.get(FIELD_DISABLED) != null)keyword.setDisabled((boolean)keywordResp.get(FIELD_DISABLED));
            if(keywordResp.get(FIELD_CPC) != null)keyword.setCpc((Integer)keywordResp.get(FIELD_CPC));
            if(keywordResp.get(FIELD_URL) != null)keyword.setUrl((String)keywordResp.get(FIELD_URL));
            if(keywordResp.get(FIELD_CREATE_DATE) != null)keyword.setCreateDate(new DateTime(keywordResp.get(FIELD_CREATE_DATE)));
            if(keywordResp.get(FIELD_GROUP_ID) != null)keyword.setGroupId((Integer)keywordResp.get(FIELD_GROUP_ID));
            if(keywordResp.get(FIELD_MIN_CPC) != null)keyword.setMinCpc((Integer)keywordResp.get(FIELD_MIN_CPC));

            return keyword; 
        } catch (NumberFormatException ex){
            throw new InvalideRequestException(ex);
        }
        
    }

}
