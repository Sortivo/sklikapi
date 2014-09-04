package cz.sortivo.sklikapi.exception;

import java.util.List;

import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.bean.Response;


public abstract class EntityCreationException extends Exception{


    private static final long serialVersionUID = 1L;
    
    private final List<Response<? extends SKlikObject>> failedEntities;
    
    public EntityCreationException(String message, List<Response<? extends SKlikObject>> entityResponses, Throwable cause) {
        super(message, cause);
        
        failedEntities = entityResponses;
    }

    public List<Response<? extends SKlikObject>> getFailedEntities() {
        return failedEntities;
    }

}
