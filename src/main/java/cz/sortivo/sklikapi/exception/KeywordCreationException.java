package cz.sortivo.sklikapi.exception;

import java.util.List;

import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.bean.Keyword;
import cz.sortivo.sklikapi.bean.Response;

public class KeywordCreationException extends EntityCreationException {
   
    public KeywordCreationException(String message, List<Response<? extends SKlikObject>> entityResponses,
            Throwable cause) {
        super(message, entityResponses, cause);
        // TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 1L;

}
