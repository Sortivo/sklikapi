package cz.sortivo.sklikapi.exception;

import java.util.List;

import cz.sortivo.sklikapi.bean.Keyword;
import cz.sortivo.sklikapi.bean.Response;

public class KeywordCreationException extends EntityCreationException {
    List<Response<Keyword>> failedKeywords;
    
    public KeywordCreationException(String message, List<Response<Keyword>> entityResponses, Throwable cause) {
        super(message, cause);
        failedKeywords = entityResponses;
    }

    private static final long serialVersionUID = 1L;

}
