package cz.sortivo.sklikapi.exception;

import cz.sortivo.sklikapi.Response;

/**
 * The exception is using if response status is different then 200
 * @author Jan Dufek
 */
public class SKlikException extends Exception{
    
    private final int status;
    private final Response response;
 
    public SKlikException(String message, int status, Response response) {
        super(message);
        this.status = status;
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public Response getResponse() {
        return response;
    }
    
    
    
}
