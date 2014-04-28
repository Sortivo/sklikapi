package cz.sortivo.sklikapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * http://api.sklik.cz/listKeywords.html
 * @author Jan Dufek
 */
public class KeywordDAO {
    private static final String LIST_KEYWORDS_METHOD_NAME = "listKeywords";
    private static final String CREATE_KEYWORD_METHOD_NAME = "keyword.create";
    private static final String REMOVE_KEYWORD_METHOD_NAME = "keyword.remove";
    private static final String RESTORE_KEYWORD_METHOD_NAME = "keyword.restore";
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
    
    private static final Set<String> settableAttributes = new HashSet<>();
    static{
        settableAttributes.addAll(Arrays.asList(new String[]{FIELD_CPC, FIELD_URL, FIELD_STATUS}));
    }
    
    private Client client;
    
    public KeywordDAO(Client client) {
        this.client = client;
    }
    
    public List<Keyword> listKeywords(int groupId) throws InvalidRequestException, SKlikException{
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
     * @throws InvalidRequestException
     * @throws SKlikException 
     */
    public Integer create(int groupId, Keyword keyword) throws InvalidRequestException, SKlikException{
        Map<String, Object> resp = client.sendRequest(CREATE_KEYWORD_METHOD_NAME, new Object[]{groupId, transformFromObject(keyword)});
        return (Integer)resp.get("keywordId");
    }
    
    public boolean remove(int keywordId) throws InvalidRequestException, SKlikException{
        client.sendRequest(REMOVE_KEYWORD_METHOD_NAME, new Object[]{keywordId});
        return true;
    }
    
    public void restore(int keywordId) throws InvalidRequestException, SKlikException{
        client.sendRequest(RESTORE_KEYWORD_METHOD_NAME, new Object[]{keywordId});
    }
    
    public boolean setActive(int keywordId) throws InvalidRequestException, SKlikException{
        return setAttributes(keywordId, new Attributes(Status.ACTIVE));
    }
    
    public boolean setSuspend(int keywordId) throws InvalidRequestException, SKlikException{
        return setAttributes(keywordId, new Attributes(Status.SUSPEND));
    }
    
    public boolean setAttributes(int keywordId, Attributes attributes) throws InvalidRequestException, SKlikException{
        Map<String, Object> map = new HashMap<>();
        
        boolean attributeSet = false;
        if (attributes.getCpc() != null){
            if (attributes.getCpc() == -1){
                map.put(FIELD_CPC, null);
            }else{
                map.put(FIELD_CPC, attributes.getCpc());
            }
            attributeSet = true;
        }
        if (attributes.getStatus() != null){
            map.put(FIELD_STATUS, attributes.getStatus().getStatusText());
            attributeSet = true;
        }
        if (!attributeSet){
            return false;
        }

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
        if (keyword.getId() != null)map.put(FIELD_ID, keyword.getId());
        if (keyword.getName() != null)map.put(FIELD_NAME, keyword.getName());
        if (keyword.getMatchType() != null)map.put(FIELD_MATCH_TYPE, keyword.getMatchType().getMatchTypeText());
        map.put(FIELD_REMOVED, keyword.isRemoved());
        if (keyword.getStatus() != null)map.put(FIELD_STATUS, keyword.getStatus().getStatusText());
        map.put(FIELD_DISABLED, keyword.isDisabled());
        if (keyword.getCpc() != null){
            if (keyword.getCpc() == -1){
                map.put(FIELD_CPC, null);
            }else{
                map.put(FIELD_CPC, keyword.getCpc());
            }
        }
        if (keyword.getCreateDate() != null)map.put(FIELD_CREATE_DATE, keyword.getCreateDate());
        if (keyword.getUrl() != null)map.put(FIELD_URL, keyword.getUrl());
        if (keyword.getGroupId() != null)map.put(FIELD_GROUP_ID, keyword.getGroupId());
        if (keyword.getMinCpc() != null){
            if (keyword.getMinCpc() == -1){
                map.put(FIELD_MIN_CPC, null);
            }else{
                map.put(FIELD_MIN_CPC, keyword.getCpc());
            }
        }
        return map;
    }
    
    private void setMatchTypeToName(Keyword k){
        String name = k.getName();
        switch (k.getMatchType()){
            case PHRASE: k.setName("\"" + name + "\""); break;
            case EXACT: k.setName("[" + name + "]"); break;
            case NEGATIVE_BROAD: k.setName("-" + name); break;
            case NEGATIVE_PHRASE: k.setName("-\"" + name + "\""); break;
            case NEGATIVE_EXACT: k.setName("-[" + name + "]"); break;
        }
    }

    private Keyword transformToObject(Map<String, Object> keywordResp) throws InvalidRequestException {
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
            
            setMatchTypeToName(keyword);
            
            return keyword; 
        } catch (NumberFormatException ex){
            throw new InvalidRequestException(ex);
        }
        
    }

    public void setAttributes(Keyword keyword) throws InvalidRequestException, SKlikException {
        
        Map<String, Object> attributes = new HashMap<>(); 
        Map<String, Object> availableAttributes = transformFromObject(keyword);
        for(String key : availableAttributes.keySet()){
            if(settableAttributes.contains(key)){
                attributes.put(key, availableAttributes.get(key));
            }
        }
        
        
        client.sendRequest(SET_ATTRIBUTES_METHOD_NAME, new Object[]{keyword.getId(), attributes});
    }

}
