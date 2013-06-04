package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.SKlikException;
import java.util.HashMap;

/**
 * It makes response object like HashMap, if status is not 200, SKlikException is thrown
 * @author Jan Dufek
 */
public class Response extends HashMap<Object, Object> {

    public Response(Object xmlRpcResponse) throws SKlikException{
        putAll((HashMap)xmlRpcResponse);
        int status = (Integer) get("status");
        if (status >= 400){
            throw new SKlikException("Request error with status code " + status + " and status message " + get("statusMessage"), status, this);
        }
    }
    
}
