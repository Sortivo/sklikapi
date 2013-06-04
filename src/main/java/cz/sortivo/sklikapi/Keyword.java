package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalideRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * http://api.sklik.cz/listKeywords.html
 * @author Jan Dufek
 */
public class Keyword {
    private static final String LIST_KEYWORDS_METHOD_NAME = "listKeywords";
    private static final String CREATE_KEYWORD_METHOD_NAME = "keyword.create";
    private static final String REMOVE_KEYWORD_METHOD_NAME = "keyword.remove";
    
    private Client client;
    
    public Keyword(Client client) {
        this.client = client;
    }
    
    public Response listKeywords(int groupId) throws InvalideRequestException, SKlikException{
        return client.sendRequest(LIST_KEYWORDS_METHOD_NAME, new Object[]{groupId});
    }
        
    public Response create(int groupId, Struct keyword) throws InvalideRequestException, SKlikException{
        return client.sendRequest(CREATE_KEYWORD_METHOD_NAME, new Object[]{groupId, keyword});
    }
    
    public Response remove(int keywordId) throws InvalideRequestException, SKlikException{
        return client.sendRequest(REMOVE_KEYWORD_METHOD_NAME, new Object[]{keywordId});
    }
}
