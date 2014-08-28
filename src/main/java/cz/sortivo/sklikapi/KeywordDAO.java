package cz.sortivo.sklikapi;

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

import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;


public class KeywordDAO {
    private static final String LIST_KEYWORDS_METHOD_NAME = "keywords.list";
    private static final String CREATE_KEYWORD_METHOD_NAME = "keyword.create";
    private static final String REMOVE_KEYWORD_METHOD_NAME = "keyword.remove";
    private static final String RESTORE_KEYWORD_METHOD_NAME = "keyword.restore";
    private static final String UPDATE_METHOD_NAME = "keywords.update";

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
    


    public List<Keyword> listKeywords(List<Integer> groupIds, boolean includeDeleted, Integer userId) throws InvalidRequestException, SKlikException {
        List<Keyword> keywords = new ArrayList<>();
        
        if(groupIds.size() > 100){
            keywords.addAll(listKeywords(groupIds.subList(0, groupIds.size()/2), includeDeleted, userId));
            keywords.addAll(listKeywords(groupIds.subList(groupIds.size()/2, groupIds.size()), includeDeleted, userId));
            return keywords;
        }
        
        
        Map<String, Object> restrictionFilter = new LinkedHashMap<>();
        restrictionFilter.put("groupIds", groupIds);
        restrictionFilter.put("includeDeleted", includeDeleted);

        Map<String, Object> response = client.sendRequest(LIST_KEYWORDS_METHOD_NAME, new Object[]{restrictionFilter}, userId);
        
        
        for (Object object : (Object[])response.get("keywords")) {
            keywords.add(transformToObject((Map<String, Object>) object));
        }
        
        return keywords;
    }
    
    public Map<String, Object> pause(List<Keyword> keywodrs, Integer userId) throws InvalidRequestException, SKlikException{
        List<Map<String, Object>> kwsList = new LinkedList<>();
        Map<String, Object> kwMap;
        for (Keyword keyword : keywodrs) {
    
            kwMap = new LinkedHashMap<>();
            if(keyword.getMatchType() == MatchType.NEGATIVE_BROAD){
                continue;
            }
            
            kwMap.put("id", keyword.getId());
            kwMap.put("status", "suspend");
            kwsList.add(kwMap);
        }

        return client.sendRequest(UPDATE_METHOD_NAME, new Object[]{kwsList}, userId);
    }
        
  
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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



}
