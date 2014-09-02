package cz.sortivo.sklikapi.bean;

import java.awt.List;


public class Response {
    
    private int status;
    private String statusMessage;
    
    

    public Response(int status, String statusMessage) {
        super();
        this.status = status;
        this.statusMessage = statusMessage;
    }

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

   
}
