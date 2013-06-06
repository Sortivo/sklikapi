package cz.sortivo.sklikapi;

import java.awt.List;

/**
 * It makes response object like HashMap, if status is not 200, SKlikException is thrown
 * @deprecated 
 * @author Jan Dufek
 */
public class Response {
    
    private int status;
    private String statusMessage;
    private String session;

//    public Response(Object xmlRpcResponse) throws SKlikException{
//        putAll((HashMap)xmlRpcResponse);
//        int status = (Integer) get("status");
//        if (status >= 400){
//            throw new SKlikException("Request error with status code " + status + " and status message " + get("statusMessage"), status, this);
//        }
//    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
    
    
    
    
}
